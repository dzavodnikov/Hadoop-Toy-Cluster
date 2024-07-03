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
