#!/usr/bin/env bash

# Hadoop starting.
# See:
#   https://hadoop.apache.org/docs/r3.3.6/hadoop-project-dist/hadoop-common/ClusterSetup.html

# # Format a new distributed filesystem as HDFS.
hdfs namenode -format -nonInteractive ${CLUSTER_NAME} \
    > /hadoop/logs/namenode_format.log 2>&1

# # Start the HDFS NameNode with the following command on the designated node as HDFS.
hdfs --daemon start namenode \
    > /hadoop/logs/namenode.log 2>&1

# Start the YARN with the following command, run on the designated ResourceManager as YARN.
yarn --daemon start resourcemanager \
    > /hadoop/logs/resourcemanager.log 2>&1

# Start a standalone WebAppProxy server.
# If multiple servers are used with load balancing it should be run on each of them.
yarn --daemon start proxyserver \
    > /hadoop/logs/proxyserver.log 2>&1

# Start the MapReduce JobHistory Server with the following command, run on the designated server as MapRed.
mapred --daemon start historyserver \
    > /hadoop/logs/historyserver.log 2>&1

# HBase starting.
# See:
#   https://hbase.apache.org/2.4/book.html#_hbase_managed_zookeeper_configuration

# Start Master.
hbase master    start \
    > /hbase/logs/hbase_master.log 2>&1 \
    &

# Start Thrift2.
HBASE_THRIFT_PORT=9090
HBASE_THRIFT_WEBUI_PORT=9091
hbase thrift2   start \
    -p ${HBASE_THRIFT_PORT} \
    --infoport ${HBASE_THRIFT_WEBUI_PORT} \
    > /hbase/logs/hbase_thrift2.log 2>&1 \
    &

# Start REST.
HBASE_REST_PORT=8090
HBASE_REST_WEBUI_PORT=8091
hbase rest      start \
    -p ${HBASE_REST_PORT} \
    --infoport ${HBASE_REST_WEBUI_PORT} \
    > /hbase/logs/hbase_rest.log 2>&1 \
    &

sleep infinity
