FROM oraclelinux:9

# For testing use:
#   % docker run --rm -it --entrypoint bash oraclelinux:9

RUN dnf -y install wget hostname

# See:
#   https://cwiki.apache.org/confluence/display/HADOOP/Hadoop+Java+Versions/
#   https://hbase.apache.org/book.html#basic.prerequisites
RUN dnf -y install java-11-openjdk
ENV JAVA_HOME=/usr/lib/jvm/jre

# Hadoop
RUN wget http://archive.apache.org/dist/hadoop/core/hadoop-3.3.6/hadoop-3.3.6.tar.gz
RUN tar -xvf hadoop-3.3.6.tar.gz && mv hadoop-3.3.6 hadoop
ENV HADOOP_HOME=/hadoop
ENV HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop
ENV PATH=${PATH}:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin

# HBase
RUN wget https://archive.apache.org/dist/hbase/2.4.17/hbase-2.4.17-bin.tar.gz
RUN tar -xvf hbase-2.4.17-bin.tar.gz && mv hbase-2.4.17 hbase
ENV HBASE_HOME=/hbase
ENV PATH=${PATH}:${HBASE_HOME}/bin

# ENTRYPOINT will be defined by Docker Compose
