#!/usr/bin/env bash

# Hadoop starting.
# See:
#   https://hadoop.apache.org/docs/r2.6.0/hadoop-project-dist/hadoop-common/ClusterSetup.html

# Start a HDFS DataNode with the following command on each designated node as HDFS.
hdfs --daemon start datanode

# Run a script to start a NodeManager on each designated host as YARN.
yarn --daemon start nodemanager

# HBase starting.
# See:
#   https://hbase.apache.org/2.4/book.html#_hbase_managed_zookeeper_configuration

# Start Hbase RegionServer on all slaves.
hbase regionserver  start > /hbase/logs/hbase_regionserver.log 2>&1 &

sleep infinity
