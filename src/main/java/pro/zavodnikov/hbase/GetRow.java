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

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;

import com.beust.jcommander.Parameter;

/**
 * Get row to HBase table.
 */
public class GetRow extends AbstractHBaseProcessor {

    @Parameter(names = { "--table" }, description = "Table name.", required = true)
    protected String tableName;

    @Parameter(names = { "--key" }, description = "Row key.", required = true)
    protected String rowKey;

    @Parameter(names = {
            "--column" }, description = "Column name in format 'FAMILY_NAME:COLUMN_NAME'.", required = true)
    protected String columnName;

    @Override
    public void process(final Connection conn, final Admin admin) throws IOException {
        final String[] columnFrags = split(this.columnName, ":", 2,
                "Expected format of column name is 'FAMILY_NAME:COLUMN_NAME'.");
        final String value = HBaseUtils.getRow(conn, admin, TableName.valueOf(this.tableName), this.rowKey,
                columnFrags[0], columnFrags[1]);
        HBaseUtils.log("Latest version value is: %s", value);
    }

    public static void main(final String[] args) throws IOException {
        final GetRow getter = new GetRow();
        getter.init(args);
        getter.run();
    }
}
