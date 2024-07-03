package pro.zavodnikov.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;

import com.beust.jcommander.Parameter;

/**
 * Get row to HBase table.
 */
public class GetRow extends AbstractHBaseProcessor {

    @Parameter(names = { "--table" }, required = true, description = "Table name")
    private String tableName;

    @Parameter(names = { "--key" }, required = true, description = "Table row key")
    private String rowKey;

    @Parameter(names = { "--family" }, required = true, description = "Table column family name")
    private String familyName;

    @Parameter(names = { "--column" }, required = true, description = "Table column name")
    private String columnName;

    @Override
    public void process(final Connection conn, final Admin admin) throws IOException {
        final String value = HBaseUtils.getRow(conn, admin, this.tableName, this.rowKey, this.familyName,
                this.columnName);
        HBaseUtils.log("Latest version value is: %s", value);
    }

    public static void main(final String[] args) throws IOException {
        final GetRow getter = new GetRow();
        getter.init(args);
        getter.run();
    }

}
