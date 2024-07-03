package pro.zavodnikov.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.TableDescriptor;

/**
 * Read names of HBase tables.
 */
public class ListTables extends AbstractHBaseProcessor {

    @Override
    public void process(final Connection conn, final Admin admin) throws IOException {
        HBaseUtils.log("Tables:");
        for (TableDescriptor table : admin.listTableDescriptors()) {
            HBaseUtils.log(" > %s", table.getTableName().getNameAsString());

            for (ColumnFamilyDescriptor family : table.getColumnFamilies()) {
                HBaseUtils.log("    * %s", family.getNameAsString());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        final ListTables reader = new ListTables();
        reader.init(args);
        reader.run();
    }
}
