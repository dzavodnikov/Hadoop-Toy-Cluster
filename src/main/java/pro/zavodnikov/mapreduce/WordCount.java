/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023-2025 Dmitry Zavodnikov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pro.zavodnikov.mapreduce;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.beust.jcommander.Parameter;

/**
 * Example of word counting.
 */
// See:
// https://hadoop.apache.org/docs/r3.3.6/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html
public class WordCount extends AbstractJobRunner {

    @Parameter(names = { "--input", "-i" }, required = true, description = "Input directory.")
    private String input;

    @Parameter(names = { "--output", "-o" }, required = true, description = "Output directory.")
    private String output;

    @Override
    protected void defineJob(final Job job) throws IllegalArgumentException, IOException {
        final Path inPath = new Path(this.input);
        final Path outPath = new Path(this.output);

        // Checking HDFS.
        final FileSystem fs = FileSystem.get(this.config);
        if (fs.exists(outPath)) {
            System.out.println(String.format("Directory '%s' exists: delete", outPath.getName()));
            fs.delete(outPath, true);
        }

        // 1. Map.
        job.setMapperClass(TokenizerMapper.class);

        // 2. Combine.
        job.setCombinerClass(IntSumReducer.class);

        // 3. Reduce.
        job.setReducerClass(IntSumReducer.class);

        // Output.
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // In/Out.
        FileInputFormat.addInputPath(job, inPath);
        FileOutputFormat.setOutputPath(job, outPath);
    }

    // Classes are: keyIn, valueIn, keyOut, valueOut
    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

        public void map(final Object key, final Text value, final Context context)
                throws IOException, InterruptedException {
            final StringTokenizer itr = new StringTokenizer(value.toString()); // Split up line to words.
            while (itr.hasMoreTokens()) {
                final Text keyOut = new Text(itr.nextToken());
                final IntWritable valueOut = new IntWritable(1);
                context.write(keyOut, valueOut);
            }
        }
    }

    // Classes are: keyIn, valueIn, keyOut, valueOut
    public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

        public void reduce(final Text key, final Iterable<IntWritable> values, final Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }

            final IntWritable valueOut = new IntWritable(sum);
            context.write(key, valueOut);
        }
    }

    public static void main(final String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        final WordCount counter = new WordCount();
        counter.init(args);
        System.exit(counter.runJob("word count") ? 0 : 1);
    }
}
