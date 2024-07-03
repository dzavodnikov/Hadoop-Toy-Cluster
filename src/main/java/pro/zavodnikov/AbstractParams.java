package pro.zavodnikov;

import org.apache.hadoop.conf.Configuration;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * Class that useful for parameter initialization.
 *
 * Just add fields with {@link Parameter} annotation and call constructor.
 */
public abstract class AbstractParams {

    @Parameter(names = { "--help", "-h" }, help = true, description = "Print help")
    private boolean help = false;

    /**
     * Initialized parameters. <strong>Do not call it in constructor!</strong>
     */
    public void init(final String[] args) {
        final JCommander commander = JCommander.newBuilder().addObject(this).build();
        commander.setProgramName(getClass().getCanonicalName());
        commander.parse(args);

        if (this.help) {
            commander.usage();
            System.exit(0);
        }
    }

    protected void initConfig(final Configuration config, final String[] requiredProperties,
            final String[] optionalProperties) {
        for (String key : requiredProperties) {
            final String value = System.getProperty(key);
            if (value == null) {
                throw new RuntimeException(String.format("Property '%s' was not defined", key));
            }
            System.out.println(String.format("    %s: %s", key, value));
            config.set(key, value);
        }

        for (String key : optionalProperties) {
            final String value = System.getProperty(key);
            if (value != null) {
                System.out.println(String.format("    %s: %s", key, value));
                config.set(key, value);
            }
        }
    }
}
