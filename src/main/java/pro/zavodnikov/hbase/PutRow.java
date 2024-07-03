package pro.zavodnikov.hbase;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;

import com.beust.jcommander.Parameter;

/**
 * Add row to HBase table.
 */
public class PutRow extends AbstractHBaseProcessor {

    @Parameter(names = { "--table" }, required = true, description = "Table name")
    private String tableName;

    @Parameter(names = { "--key" }, required = true, description = "Table row key")
    private String rowKey;

    @Parameter(names = { "--family" }, required = true, description = "Table column family name")
    private String familyName;

    @Parameter(names = { "--column" }, required = true, description = "Table column name")
    private String columnName;

    // To make possible provide strings with spaces.
    @Parameter(names = { "--value", "-a" }, required = true, variableArity = true, description = "Table column value")
    private List<String> value;

    @Override
    public void process(final Connection conn, final Admin admin) throws IOException {
        HBaseUtils.putRow(conn, admin, this.tableName, this.rowKey, this.familyName, this.columnName,
                String.join(" ", this.value));
    }

    public static void main(final String[] args) throws IOException {
        final PutRow setter = new PutRow();
        setter.init(args);
        setter.run();
    }
}
