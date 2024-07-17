package pro.zavodnikov.mapreduce;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.TaskAttemptID;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.task.TaskAttemptContextImpl;
import org.apache.hadoop.util.ReflectionUtils;
import org.junit.jupiter.api.Test;

public class TextInputFormatRecordReaderTest {

    private static final String FILE = "file.txt";

    public static <K, V, T extends InputFormat<K, V>> RecordReader<K, V> createRecordReader(final String filepath,
            final Class<T> cl) throws IOException, InterruptedException {
        final Configuration conf = new Configuration(false);
        conf.set("fs.default.name", "file:///");

        final File file = new File(filepath);
        final Path path = new Path(file.getAbsoluteFile().toURI());
        final FileSplit split = new FileSplit(path, 0, file.length(), null);

        final T inputFormat = ReflectionUtils.newInstance(cl, conf);
        final TaskAttemptContext context = new TaskAttemptContextImpl(conf, new TaskAttemptID());
        final RecordReader<K, V> reader = inputFormat.createRecordReader(split, context);
        reader.initialize(split, context);
        return reader;
    }

    public static String resourceFilepath(final String filename) {
        return Thread.currentThread().getContextClassLoader().getResource(filename).getFile();
    }

    @Test
    public void testReadingTextFile() throws IOException, InterruptedException {
        final RecordReader<LongWritable, Text> reader = createRecordReader(resourceFilepath(FILE),
                TextInputFormat.class);

        final List<String> values = new ArrayList<>();
        while (reader.nextKeyValue()) {
            final String value = reader.getCurrentValue().toString();
            values.add(value);
        }

        assertEquals(2, values.size());
        assertEquals("Hello World", values.get(0));
        assertEquals("Hello MapReduce", values.get(1));
    }
}
