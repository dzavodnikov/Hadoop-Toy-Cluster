#!/usr/bin/env bash

# ----------------------------------------------------------------------------------------------------------------------
# Hadoop.
# See:
#   https://hadoop.apache.org/docs/r3.4.2/hadoop-project-dist/hadoop-common/ClusterSetup.html
# ----------------------------------------------------------------------------------------------------------------------
# Create log dir.
mkdir -p "${HADOOP_LOG_DIR}"

# Start a HDFS DataNode with the following command on each designated node as HDFS.
hdfs --daemon start datanode \
    > "${HADOOP_LOG_DIR}/datanode.log" 2>&1

# Run a script to start a NodeManager on each designated host as YARN.
yarn --daemon start nodemanager \
    > "${HADOOP_LOG_DIR}/nodemanager.log" 2>&1

# ----------------------------------------------------------------------------------------------------------------------
# HBase.
# See:
#   https://hbase.apache.org/2.4/book.html#_hbase_managed_zookeeper_configuration
# ----------------------------------------------------------------------------------------------------------------------
# Create log dir.
mkdir -p "${HBASE_LOG_DIR}"

# Start Hbase RegionServer on all slaves.
hbase regionserver  start \
    > "${HBASE_LOG_DIR}/regionserver.log" 2>&1 \
    &

# ----------------------------------------------------------------------------------------------------------------------
# Run container.
# ----------------------------------------------------------------------------------------------------------------------
sleep infinity
