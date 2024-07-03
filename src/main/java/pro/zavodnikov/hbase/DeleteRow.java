package pro.zavodnikov.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;

import com.beust.jcommander.Parameter;

/**
 * Delete some records in HBase table.
 */
public class DeleteRow extends AbstractHBaseProcessor {

    @Parameter(names = { "--table" }, required = true, description = "Table name")
    private String tableName;

    @Parameter(names = { "--key" }, required = true, description = "Table row key")
    private String rowKey;

    @Override
    public void process(final Connection conn, final Admin admin) throws IOException {
        HBaseUtils.deleteRow(conn, admin, tableName, rowKey);
    }

    public static void main(String[] args) throws IOException {
        final DeleteRow cleaner = new DeleteRow();
        cleaner.init(args);
        cleaner.run();
    }
}
