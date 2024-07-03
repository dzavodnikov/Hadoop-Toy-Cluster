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
 * Create column Family to existing HBase table.
 */
public class AddTableFamily extends AbstractHBaseProcessor {

    @Parameter(names = { "--table", }, required = true, description = "Table name to create.")
    protected String tableName;

    @Parameter(names = { "--family" }, required = true, description = "Family of that table. Can be more than one.")
    protected String familyName;

    @Parameter(names = { "--max-versions" }, description = "Max versions of values in same cell.")
    protected Integer maxVersions = 1;

    @Parameter(names = { "--ttl-seconds" }, description = "Max Time-To-Live of values in same cell.")
    protected Integer ttlSec = Integer.MAX_VALUE;

    @Override
    public void process(final Connection conn, final Admin admin) throws IOException {
        HBaseUtils.createFamily(conn, admin, TableName.valueOf(this.tableName), this.familyName, this.maxVersions,
                this.ttlSec);
    }

    public static void main(final String[] args) throws IOException {
        final AddTableFamily creator = new AddTableFamily();
        creator.init(args);
        creator.run();
    }
}
