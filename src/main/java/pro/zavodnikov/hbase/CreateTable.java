package pro.zavodnikov.hbase;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;

import com.beust.jcommander.Parameter;

/**
 * Create HBase table.
 */
public class CreateTable extends AbstractHBaseProcessor {

    @Parameter(names = { "--table" }, required = true, description = "Table name to create")
    private String tableName;

    @Parameter(names = { "--family" }, required = true, description = "Family of that table. Can be more than one")
    private List<String> family;

    @Override
    public void process(final Connection conn, final Admin admin) throws IOException {
        HBaseUtils.createTable(conn, admin, this.tableName, this.family);
    }

    public static void main(final String[] args) throws IOException {
        final CreateTable creator = new CreateTable();
        creator.init(args);
        creator.run();
    }
}
