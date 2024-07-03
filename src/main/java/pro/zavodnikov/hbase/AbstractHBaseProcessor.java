package pro.zavodnikov.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import pro.zavodnikov.AbstractParams;

/**
 * Get access to HBase.
 */
public abstract class AbstractHBaseProcessor extends AbstractParams {

    protected static final String[] REQ_PROPS = {
            HConstants.ZOOKEEPER_QUORUM,
    };

    protected static final String[] OPT_PROPS = {
            HConstants.HBASE_CLIENT_SCANNER_CACHING
    };

    protected final Configuration config;

    protected AbstractHBaseProcessor() {
        this.config = HBaseConfiguration.create();
        initConfig(this.config, REQ_PROPS, OPT_PROPS);
    }

    public abstract void process(Connection conn, Admin admin) throws IOException;

    /**
     * Make a request to HBase.
     *
     * @throws IOException
     */
    public void run() throws IOException {
        try (Connection conn = ConnectionFactory.createConnection(this.config); Admin admin = conn.getAdmin()) {
            process(conn, admin);
        }
    }
}
