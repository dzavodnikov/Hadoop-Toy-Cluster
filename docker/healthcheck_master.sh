#!/usr/bin/env bash

source healthcheck.sh

check_ports 9870    # HDFS NameNode
check_ports 8088    # YARN ResourceManager
check_ports 19888   # Hadoop MapReduce JobHistory Server
check_ports 16010   # HBase Web UI
check_ports 9091    # HBase Thrift Web UI
check_ports 8090    # HBase REST client
check_ports 8091    # HBase REST Web UI

exit 0
