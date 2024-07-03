#!/usr/bin/env bash

# ----------------------------------------------------------------------------------------------------------------------
# Hadoop.
# See:
#   https://hadoop.apache.org/docs/r3.4.2/hadoop-project-dist/hadoop-common/ClusterSetup.html
# ----------------------------------------------------------------------------------------------------------------------
# Create log dir.
mkdir -p "${HADOOP_LOG_DIR}"

# Format a new distributed filesystem as HDFS.
hdfs namenode -format -nonInteractive "${CLUSTER_NAME}" \
    > "${HADOOP_LOG_DIR}/namenode_format.log" 2>&1

# Start the HDFS NameNode with the following command on the designated node as HDFS.
hdfs    --daemon start namenode \
    > "${HADOOP_LOG_DIR}/namenode.log" 2>&1

# Start the YARN with the following command, run on the designated ResourceManager as YARN.
yarn    --daemon start resourcemanager \
    > "${HADOOP_LOG_DIR}/resourcemanager.log" 2>&1

# Start the MapReduce JobHistory Server with the following command, run on the designated server as MapRed.
mapred  --daemon start historyserver \
    > "${HADOOP_LOG_DIR}/historyserver.log" 2>&1

# ----------------------------------------------------------------------------------------------------------------------
# HBase.
# See:
#   https://hbase.apache.org/2.4/book.html#_hbase_managed_zookeeper_configuration
# ----------------------------------------------------------------------------------------------------------------------
# Create log dir.
mkdir -p "${HBASE_LOG_DIR}"

# Start Master.
hbase master    start \
    > "${HBASE_LOG_DIR}/master.log" 2>&1 \
    &

# Start Thrift2.
HBASE_THRIFT_PORT=9090
HBASE_THRIFT_WEBUI_PORT=9091
hbase thrift2   start \
    -p "${HBASE_THRIFT_PORT}" \
    --infoport "${HBASE_THRIFT_WEBUI_PORT}" \
    > "${HBASE_LOG_DIR}/thrift2.log" 2>&1 \
    &

# Start REST.
HBASE_REST_PORT=8090
HBASE_REST_WEBUI_PORT=8091
hbase rest      start \
    -p "${HBASE_REST_PORT}" \
    --infoport "${HBASE_REST_WEBUI_PORT}" \
    > "${HBASE_LOG_DIR}/rest.log" 2>&1 \
    &

# ----------------------------------------------------------------------------------------------------------------------
# Run container.
# ----------------------------------------------------------------------------------------------------------------------
sleep infinity
