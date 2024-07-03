package pro.zavodnikov.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder.ModifyableColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Utility class to manipulate with HBase.
 */
public final class HBaseUtils {

    public static void log(final String format, final Object... args) {
        if (args == null || args.length == 0) {
            System.out.println(format);
        } else {
            System.out.println(String.format(format, args));
        }
    }

    public static boolean tableExists(final Admin admin, final String name) throws IOException {
        for (TableDescriptor table : admin.listTableDescriptors()) {
            if (table.getTableName().getNameAsString().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static String tableNamespace(final String name) throws IOException {
        final String[] frags = name.split(":");
        return frags.length > 1 ? frags[0] : null;
    }

    public static boolean namespaceExists(final Admin admin, final String name) throws IOException {
        for (NamespaceDescriptor namespace : admin.listNamespaceDescriptors()) {
            if (namespace.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean familyExists(final Admin admin, final String tableName, final String familyName)
            throws IOException {
        final TableDescriptor table = admin.getDescriptor(TableName.valueOf(tableName));
        for (ColumnFamilyDescriptor family : table.getColumnFamilies()) {
            if (family.getNameAsString().equals(familyName)) {
                return true;
            }
        }
        return false;
    }

    public static void createFamily(final Connection conn, final Admin admin, final String tableName,
            final String familyName, final int maxVersions, final int ttlSec) throws IOException {
        if (!tableExists(admin, tableName)) {
            log("Table '%s' is not exists", tableName);
            return;
        }
        final TableName tn = TableName.valueOf(tableName);

        final ModifyableColumnFamilyDescriptor family = new ModifyableColumnFamilyDescriptor(familyName.getBytes());
        family.setMaxVersions(maxVersions);
        family.setTimeToLive(ttlSec);
        admin.addColumnFamily(tn, family); // Override table definition.

        log("Table '%s' updated", tableName);
    }

    public static void createFamily(final Connection conn, final Admin admin, final String tableName,
            final String familyName) throws IOException {
        createFamily(conn, admin, tableName, familyName, 1, Integer.MAX_VALUE);
    }

    public static void createNamespace(final Admin admin, final String namespaceName) throws IOException {
        if (namespaceExists(admin, namespaceName)) {
            log("Namespace '%s' exists", namespaceName);
            return;
        }

        admin.createNamespace(NamespaceDescriptor.create(namespaceName).build());

        log("Namespace '%s' created", namespaceName);
    }

    public static void createTable(final Connection conn, final Admin admin, final String tableName,
            final List<String> families) throws IOException {
        if (tableExists(admin, tableName)) {
            for (final String familyName : families) {
                if (!familyExists(admin, tableName, familyName)) {
                    createFamily(conn, admin, tableName, familyName);
                }
            }
            log("Table '%s' exists", tableName);
        } else {
            final String namespaceName = tableNamespace(tableName);
            if (namespaceName != null) {
                createNamespace(admin, namespaceName);
            }

            final TableDescriptorBuilder table = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));

            final List<ColumnFamilyDescriptor> tFamilies = new ArrayList<>();
            for (String familyName : families) {
                tFamilies.add(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(familyName)).build());
            }
            table.setColumnFamilies(tFamilies);

            admin.createTable(table.build());

            final List<String> tableDef = new ArrayList<>();
            tableDef.add(tableName);
            tableDef.addAll(families);
            log("Table '%s' created", String.join(", ", tableDef));
        }
    }

    public static void deleteTable(final Connection conn, final Admin admin, final String tableName)
            throws IOException {
        if (!tableExists(admin, tableName)) {
            log("Table '%s' is not exists", tableName);
            return;
        }
        final TableName tn = TableName.valueOf(tableName);

        admin.disableTable(tn);
        admin.deleteTable(tn);

        log("Table '%s' dropped", tableName);
    }

    public static String getRow(final Connection conn, final Admin admin, final String tableName, final String rowKey,
            final String familyName, final String columnName) throws IOException {
        if (!tableExists(admin, tableName)) {
            log("Table '%s' is not exists", tableName);
            return null;
        }
        final Table table = conn.getTable(TableName.valueOf(tableName));

        final Get get = new Get(Bytes.toBytes(rowKey));
        final Result res = table.get(get);
        final byte[] value = res.getValue(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
        return Bytes.toString(value);
    }

    public static void putRow(final Connection conn, final Admin admin, final String tableName, final String rowKey,
            final String familyName, final String columnName, final String value) throws IOException {
        if (!tableExists(admin, tableName)) {
            log("Table '%s' is not exists", tableName);
            return;
        }
        final Table table = conn.getTable(TableName.valueOf(tableName));

        final Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName), Bytes.toBytes(value));
        table.put(put);

        log("Value added to the table '%s' with key '%s' at '%s:%s'", tableName, rowKey, familyName, columnName);
    }

    public static void deleteRow(final Connection conn, final Admin admin, final String tableName, final String rowKey)
            throws IOException {
        if (!tableExists(admin, tableName)) {
            log("Table '%s' is not exists", tableName);
            return;
        }
        final Table table = conn.getTable(TableName.valueOf(tableName));

        final Delete rowDel = new Delete(rowKey.getBytes());
        table.delete(rowDel);

        log("Row '%s' deleted from '%s'", rowKey, tableName);
    }
}
