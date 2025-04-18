FROM oraclelinux:9

# For testing use:
#   $ docker run --interactive --tty --rm --entrypoint bash oraclelinux:9

RUN dnf -y install hostname

# ----------------------------------------------------------------------------------------------------------------------
# Java
# ----------------------------------------------------------------------------------------------------------------------
RUN dnf -y install java-11-openjdk java-11-openjdk-devel
ENV JAVA_HOME=/usr/lib/jvm/java

# ----------------------------------------------------------------------------------------------------------------------
# Maven
# ----------------------------------------------------------------------------------------------------------------------
ENV MAVEN_VERSION=3.9.9
ENV MAVEN_DISTR="apache-maven-${MAVEN_VERSION}"
ENV MAVEN_ARCH="${MAVEN_DISTR}-bin.tar.gz"

# Download Maven
RUN curl -L -O "https://dlcdn.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/${MAVEN_ARCH}"
RUN tar -xf "${MAVEN_ARCH}"
RUN rm -rf "${MAVEN_ARCH}"

# Install Maven
RUN mv "${MAVEN_DISTR}" /usr/share
RUN ln -s "/usr/share/${MAVEN_DISTR}/bin/mvn" /usr/bin/mvn

# ----------------------------------------------------------------------------------------------------------------------
# Hadoop
# ----------------------------------------------------------------------------------------------------------------------
ARG HADOOP_VER=3.3.5
ARG HADOOP_DISTR=hadoop-${HADOOP_VER}
ARG HADOOP_ARCH=${HADOOP_DISTR}.tar.gz
RUN curl -L -O http://archive.apache.org/dist/hadoop/core/${HADOOP_DISTR}/${HADOOP_ARCH}
RUN tar -xf "${HADOOP_ARCH}" && mv "${HADOOP_DISTR}" /usr/local/hadoop
RUN rm -rf "${HADOOP_ARCH}"
ENV HADOOP_HOME=/usr/local/hadoop
ENV HADOOP_CONF_DIR="${HADOOP_HOME}/etc/hadoop"
ENV HADOOP_LOG_DIR=/var/log/hadoop
ENV PATH=${PATH}:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin

# ----------------------------------------------------------------------------------------------------------------------
# Azkaban
# ----------------------------------------------------------------------------------------------------------------------
ARG AZKABAN_VER=4.0.0
ARG AZKABAN_DISTR=azkaban-${AZKABAN_VER}
ARG AZKABAN_ARCH=${AZKABAN_DISTR}.tar.gz
COPY ${AZKABAN_ARCH} /
RUN tar -xf ${AZKABAN_ARCH} && mv /azkaban /opt/azkaban
RUN rm -rf "${AZKABAN_ARCH}"

# ----------------------------------------------------------------------------------------------------------------------
# Spark
# ----------------------------------------------------------------------------------------------------------------------
ARG SPARK_VER=3.5.5
ARG SPARK_DISTR=spark-${SPARK_VER}-bin-without-hadoop
ARG SPARK_ARCH=${SPARK_DISTR}.tgz
RUN curl -L -O "https://dlcdn.apache.org/spark/spark-${SPARK_VER}/${SPARK_ARCH}"
RUN tar -xf "${SPARK_ARCH}" && mv "${SPARK_DISTR}" /usr/local/spark
RUN rm -rf "${SPARK_ARCH}"
ENV SPARK_HOME=/usr/local/spark
ENV SPARK_CONF_DIR=/etc/spark
ENV SPARK_LOG_DIR=/var/log/spark
ENV PATH="${PATH}:${SPARK_HOME}/bin"

# ----------------------------------------------------------------------------------------------------------------------
# HBase
# ----------------------------------------------------------------------------------------------------------------------
ARG HBASE_VER=2.6.1
ARG HBASE_DISTR=hbase-${HBASE_VER}-hadoop3
ARG HBASE_ARCH=${HBASE_DISTR}-bin.tar.gz
RUN curl -L -O https://archive.apache.org/dist/hbase/${HBASE_VER}/${HBASE_ARCH}
RUN tar -xf "${HBASE_ARCH}" && mv "${HBASE_DISTR}" /usr/local/hbase
RUN rm -rf "${HBASE_ARCH}"
ENV HBASE_HOME=/usr/local/hbase
ENV HBASE_CONF_DIR="${HBASE_HOME}/conf"
ENV HBASE_LOG_DIR=/var/log/hbase
ENV PATH=${PATH}:${HBASE_HOME}/bin

# ----------------------------------------------------------------------------------------------------------------------
# Utility scripts
# ----------------------------------------------------------------------------------------------------------------------
COPY ./entrypoint-master.sh /entrypoint-master.sh
COPY ./entrypoint-worker.sh /entrypoint-worker.sh
COPY ./ports.sh /usr/bin/ports

# ENTRYPOINT will be defined by Docker Compose
