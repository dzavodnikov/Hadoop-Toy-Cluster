#!/usr/bin/env bash

# Hadoop starting.
# See:
#   https://hadoop.apache.org/docs/r3.3.6/hadoop-project-dist/hadoop-common/ClusterSetup.html

# Start a HDFS DataNode with the following command on each designated node as HDFS.
hdfs --daemon start datanode \
    > "${HADOOP_LOG_DIR}/datanode.log" 2>&1

# Run a script to start a NodeManager on each designated host as YARN.
yarn --daemon start nodemanager \
    > "${HADOOP_LOG_DIR}/nodemanager.log" 2>&1

# HBase starting.
# See:
#   https://hbase.apache.org/2.4/book.html#_hbase_managed_zookeeper_configuration

# Start Hbase RegionServer on all slaves.
hbase regionserver  start \
    > "${HBASE_LOG_DIR}/regionserver.log" 2>&1 \
    &

sleep infinity
