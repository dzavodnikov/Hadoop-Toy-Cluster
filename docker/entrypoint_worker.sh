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

# Spark starting.
# See:
#   https://spark.apache.org/docs/3.5.1/spark-standalone.html

# Start the Worker node.
SPARK_MASTER=spark://master1.toy:7077
SPARK_SERVICE_PORT=7078
SPARK_WEBUI_PORT=8081
SPARK_WORK_DIR=/data/spark
"${SPARK_HOME}/sbin/start-worker.sh" \
    --host          "$(hostname -f)" \
    --port          "${SPARK_SERVICE_PORT}" \
    --webui-port    "${SPARK_WEBUI_PORT}" \
    --work-dir      "${SPARK_WORK_DIR}" \
    "${SPARK_MASTER}" \
    > "${SPARK_LOG_DIR}/worker.log" 2>&1

# HBase starting.
# See:
#   https://hbase.apache.org/2.4/book.html#_hbase_managed_zookeeper_configuration

# Start Hbase RegionServer on all slaves.
hbase regionserver  start \
    > "${HBASE_LOG_DIR}/regionserver.log" 2>&1 \
    &

sleep infinity
