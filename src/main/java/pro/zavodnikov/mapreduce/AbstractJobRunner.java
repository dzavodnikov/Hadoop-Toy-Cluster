package pro.zavodnikov.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;

import pro.zavodnikov.AbstractParams;

/**
 * Run Jobs on MapReduce.
 */
public abstract class AbstractJobRunner extends AbstractParams {

    protected static final String[] REQ_PROPS = {
    };

    protected static final String[] OPT_PROPS = {
            MRJobConfig.QUEUE_NAME,
            MRJobConfig.JVM_NUMTASKS_TORUN,
            MRJobConfig.MAP_JAVA_OPTS,
            MRJobConfig.REDUCE_JAVA_OPTS
    };

    protected final Configuration config;

    protected AbstractJobRunner() {
        this.config = new Configuration();
        initConfig(this.config, REQ_PROPS, OPT_PROPS);
    }

    protected abstract void defineJob(Job job) throws IllegalArgumentException, IOException;

    public boolean runJob(final String jobName) throws ClassNotFoundException, IOException, InterruptedException {
        final Job job = Job.getInstance(this.config, jobName);
        job.setJarByClass(getClass());
        defineJob(job);

        return job.waitForCompletion(true);
    }
}
