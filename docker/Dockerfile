FROM oraclelinux:9

# For testing use:
#   $ docker run --rm -it --entrypoint bash oraclelinux:9

RUN dnf -y install wget hostname jq

RUN dnf -y install java-11-openjdk
ENV JAVA_HOME=/usr/lib/jvm/jre

# Hadoop.
ARG HADOOP_VER=3.3.6
ARG HADOOP_DISTR=hadoop-${HADOOP_VER}
ARG HADOOP_ARCH=${HADOOP_DISTR}.tar.gz
RUN wget http://archive.apache.org/dist/hadoop/core/${HADOOP_DISTR}/${HADOOP_ARCH}
RUN tar -xf ${HADOOP_ARCH} && mv ${HADOOP_DISTR} /usr/local/hadoop
ENV HADOOP_HOME=/usr/local/hadoop
ENV HADOOP_CONF_DIR=/etc/hadoop
ENV HADOOP_LOG_DIR=/var/log/hadoop
RUN mkdir ${HADOOP_LOG_DIR}
ENV PATH=${PATH}:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin

# Azkaban.
ARG AZKABAN_VER=4.0.0
ARG AZKABAN_DISTR=azkaban-${AZKABAN_VER}
ARG AZKABAN_ARCH=${AZKABAN_DISTR}.tar.gz
COPY ${AZKABAN_ARCH} /
RUN tar -xf ${AZKABAN_ARCH} && mv /azkaban /opt/azkaban
ENV AZKABAN_LOG_DIR=/var/log/azkaban

# Spark.
ARG SPARK_VER=3.5.1
ARG SPARK_DISTR=spark-${SPARK_VER}-bin-without-hadoop
ARG SPARK_ARCH=${SPARK_DISTR}.tgz
RUN wget https://dlcdn.apache.org/spark/spark-${SPARK_VER}/${SPARK_ARCH}
RUN tar -xf ${SPARK_ARCH} && mv ${SPARK_DISTR} /usr/local/spark
ENV SPARK_HOME=/usr/local/spark
ENV SPARK_CONF_DIR=/etc/spark
ENV SPARK_LOG_DIR=/var/log/spark
ENV PATH=${PATH}:${SPARK_HOME}/bin

# HBase.
ARG HBASE_VER=2.6.0
ARG HBASE_DISTR=hbase-${HBASE_VER}
ARG HBASE_ARCH=${HBASE_DISTR}-bin.tar.gz
RUN wget https://archive.apache.org/dist/hbase/${HBASE_VER}/${HBASE_ARCH}
RUN tar -xf ${HBASE_ARCH} && mv ${HBASE_DISTR} /usr/local/hbase
ENV HBASE_HOME=/usr/local/hbase
ENV HBASE_CONF_DIR=/etc/hbase
ENV HBASE_LOG_DIR=/var/log/hbase
RUN mkdir ${HBASE_LOG_DIR}
ENV PATH=${PATH}:${HBASE_HOME}/bin

# Utility scripts.
COPY ./ports.sh /usr/bin/ports

# ENTRYPOINT will be defined by Docker Compose
