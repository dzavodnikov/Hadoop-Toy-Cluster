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

import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;

import com.beust.jcommander.Parameter;

/**
 * Get row to HBase table.
 */
public class GetRow extends AbstractHBaseProcessor {

    @Parameter(names = { "--table" }, required = true, description = "Table name.")
    protected String tableName;

    @Parameter(names = { "--key" }, required = true, description = "Table row key.")
    protected String rowKey;

    @Parameter(names = { "--family" }, required = true, description = "Table column family name.")
    protected String familyName;

    @Parameter(names = { "--column" }, required = true, description = "Table column name.")
    protected String columnName;

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
