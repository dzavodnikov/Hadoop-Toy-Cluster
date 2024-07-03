package pro.zavodnikov.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;

import com.beust.jcommander.Parameter;

/**
 * Drop HBase table.
 */
public class DropTable extends AbstractHBaseProcessor {

    @Parameter(names = { "--table" }, required = true, description = "Table name to drop (disable, then delete)")
    private String tableName;

    @Override
    public void process(final Connection conn, final Admin admin) throws IOException {
        HBaseUtils.deleteTable(conn, admin, this.tableName);
    }

    public static void main(final String[] args) throws IOException {
        final DropTable cleaner = new DropTable();
        cleaner.init(args);
        cleaner.run();
    }
}
