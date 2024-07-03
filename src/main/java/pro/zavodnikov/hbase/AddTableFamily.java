package pro.zavodnikov.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;

import com.beust.jcommander.Parameter;

/**
 * Create column Family to existing HBase table.
 */
public class AddTableFamily extends AbstractHBaseProcessor {

    @Parameter(names = { "--table", }, required = true, description = "Table name to create")
    private String tableName;

    @Parameter(names = { "--family" }, required = true, description = "Family of that table. Can be more than one")
    private String familyName;

    @Parameter(names = { "--max-versions" }, description = "Max versions of values in same cell")
    private Integer maxVersions = 1;

    @Parameter(names = { "--ttl-seconds" }, description = "Max Time-To-Live of values in same cell")
    private Integer ttlSec = Integer.MAX_VALUE;

    @Override
    public void process(final Connection conn, final Admin admin) throws IOException {
        HBaseUtils.createFamily(conn, admin, this.tableName, this.familyName, this.maxVersions, this.ttlSec);
    }

    public static void main(final String[] args) throws IOException {
        final AddTableFamily creator = new AddTableFamily();
        creator.init(args);
        creator.run();
    }
}
