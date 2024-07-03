package pro.zavodnikov.hbase;

import java.io.IOException;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.HRegionLocation;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.RegionLocator;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.ColumnRangeFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.beust.jcommander.Parameter;

/**
 * Scan HBase table.
 */
public class ScanTable extends AbstractHBaseProcessor {

    @Parameter(names = { "--table" }, required = true, description = "Table name to scan")
    private String tableName;

    @Parameter(names = { "--start" }, description = "Start scan key")
    private String startKey;

    @Parameter(names = { "--end" }, description = "End scan key")
    private String endKey;

    @Parameter(names = { "--limit" }, description = "Scan limit")
    private Integer limit = Integer.MAX_VALUE;

    @Parameter(names = { "--max-versions" }, description = "Max versions")
    private Integer maxVersions = Integer.MAX_VALUE;

    @Override
    public void process(final Connection conn, final Admin admin) throws IOException {
        if (!HBaseUtils.tableExists(admin, this.tableName)) {
            HBaseUtils.log("Table is not exists");
            return;
        }
        final Table table = conn.getTable(TableName.valueOf(this.tableName));

        final FilterList filterList = new FilterList();

        final byte[] start = this.startKey != null ? Bytes.toBytes(this.startKey) : null;
        final byte[] end = this.endKey != null ? Bytes.toBytes(this.endKey) : null;
        filterList.addFilter(new ColumnRangeFilter(start, true, end, true));

        filterList.addFilter(new PageFilter(this.limit));

        final Scan scan = new Scan();
        scan.readAllVersions();
        scan.setFilter(filterList);

        try (ResultScanner scanner = table.getScanner(scan)) {
            HBaseUtils.log("Scan table '%s' from host '%s':", this.tableName, System.getProperty("hostname"));
            for (final Result res : scanner) {
                final RegionLocator regLocator = conn.getRegionLocator(TableName.valueOf(this.tableName));
                final HRegionLocation regLoc = regLocator.getRegionLocation(res.getRow());
                final String regHostname = regLoc.getHostname();
                final String regName = regLoc.getRegion().getRegionNameAsString();

                final String rowKey = Bytes.toString(res.getRow());

                HBaseUtils.log("%s (located at '%s' in '%s')", rowKey, regHostname, regName);

                // Map of families to all versions of its qualifiers and values:
                // Map<family, Map<qualifier, Map<timestamp, value>>>
                final NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> rowMap = res.getMap();
                for (byte[] family : rowMap.keySet()) {
                    HBaseUtils.log(" * %s", Bytes.toString(family));

                    final NavigableMap<byte[], NavigableMap<Long, byte[]>> columnMap = rowMap.get(family);
                    for (byte[] column : columnMap.keySet()) {
                        HBaseUtils.log("    - %s", Bytes.toString(column));

                        final NavigableMap<Long, byte[]> versionMap = columnMap.get(column);
                        for (long ts : versionMap.keySet()) {
                            final String value = Bytes.toString(versionMap.get(ts));
                            HBaseUtils.log("         %s", value);
                        }
                    }
                }
            }
        }
    }

    public static void main(final String[] args) throws IOException {
        final ScanTable scanner = new ScanTable();
        scanner.init(args);
        scanner.run();
    }
}
