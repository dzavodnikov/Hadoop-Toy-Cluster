#!/usr/bin/env bash

# Hadoop starting.
# See:
#   https://hadoop.apache.org/docs/r3.3.6/hadoop-project-dist/hadoop-common/ClusterSetup.html

# Format a new distributed filesystem as HDFS.
hdfs namenode -format -nonInteractive "${CLUSTER_NAME}" \
    > "${HADOOP_LOG_DIR}/namenode_format.log" 2>&1

# Start the HDFS NameNode with the following command on the designated node as HDFS.
hdfs    --daemon start namenode \
    > "${HADOOP_LOG_DIR}/namenode.log" 2>&1

# Start the YARN with the following command, run on the designated ResourceManager as YARN.
yarn    --daemon start resourcemanager \
    > "${HADOOP_LOG_DIR}/resourcemanager.log" 2>&1

# Start a standalone WebAppProxy server.
# If multiple servers are used with load balancing it should be run on each of them.
yarn    --daemon start proxyserver \
    > "${HADOOP_LOG_DIR}/proxyserver.log" 2>&1

# Start the MapReduce JobHistory Server with the following command, run on the designated server as MapRed.
mapred  --daemon start historyserver \
    > "${HADOOP_LOG_DIR}/historyserver.log" 2>&1

# Azkaban starting. Looking for 'azkaban.properties' config in current directory.
cd /opt/azkaban && ./bin/start-solo.sh \
    > "${AZKABAN_LOG_DIR}/azkaban.log" 2>&1 \
    &

# Spark starting.
# See:
#   https://spark.apache.org/docs/3.5.1/spark-standalone.html

# Start the Master node.
SPARK_SERVICE_PORT=7077
SPARK_WEBUI_PORT=8082
"${SPARK_HOME}/sbin/start-master.sh" \
    --host          "$(hostname -f)" \
    --port          "${SPARK_SERVICE_PORT}" \
    --webui-port    "${SPARK_WEBUI_PORT}" \
    > "${SPARK_LOG_DIR}/master.log" 2>&1

# Create required HDFS directories.
ports wait 9870
hdfs dfs -mkdir -p /logs/spark/history
hdfs dfs -mkdir -p /spark

# Start the History server.
"${SPARK_HOME}/sbin/start-history-server.sh" \
    > "${SPARK_LOG_DIR}/history_server.log" 2>&1

# HBase starting.
# See:
#   https://hbase.apache.org/2.4/book.html#_hbase_managed_zookeeper_configuration

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

sleep infinity
