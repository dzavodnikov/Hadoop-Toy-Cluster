/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023-2025 Dmitry Zavodnikov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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

    @Parameter(names = { "--table" }, required = true, description = "Table name to scan.")
    protected String tableName;

    @Parameter(names = { "--start" }, description = "Start scan key.")
    protected String startKey;

    @Parameter(names = { "--end" }, description = "End scan key.")
    protected String endKey;

    @Parameter(names = { "--limit" }, description = "Scan limit.")
    protected Integer limit = Integer.MAX_VALUE;

    @Parameter(names = { "--max-versions" }, description = "Max versions.")
    protected Integer maxVersions = Integer.MAX_VALUE;

    @Override
    public void process(final Connection conn, final Admin admin) throws IOException {
        if (!HBaseUtils.tableExists(admin, TableName.valueOf(this.tableName))) {
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
            HBaseUtils.log("Scan table '%s':", this.tableName);
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
