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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import com.beust.jcommander.Parameter;

import pro.zavodnikov.AbstractParams;

/**
 * Get access to HBase.
 */
public abstract class AbstractHBaseProcessor extends AbstractParams {

    @Parameter(names = { "--zkq", }, required = true, description = "Zookeeper quorum.")
    protected String zookeeperQuorum;

    protected final Configuration config;

    protected AbstractHBaseProcessor() {
        this.config = HBaseConfiguration.create();
    }

    protected void customParametersProcess() {
        super.customParametersProcess();

        this.config.set(HConstants.ZOOKEEPER_QUORUM, this.zookeeperQuorum);
    }

    public abstract void process(Connection conn, Admin admin) throws IOException;

    /**
     * Make a request to HBase.
     */
    public void run() throws IOException {
        try (Connection conn = ConnectionFactory.createConnection(this.config); Admin admin = conn.getAdmin()) {
            process(conn, admin);
        }

        // To solve problem with threads in different environments,
        // like in Maven exec plugin.
        System.exit(0);
    }
}
