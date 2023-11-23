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

# Azkaban
RUN apk add git nodejs npm
RUN wget https://github.com/azkaban/azkaban/archive/refs/tags/4.0.0.tar.gz -O azkaban-4.0.0-src.tar.gz
RUN tar -xvf azkaban-4.0.0-src.tar.gz && mv azkaban-4.0.0 azkaban-src
# See: https://github.com/azkaban/azkaban/issues/2899/
RUN sed -i.bak "s/linkedin.bintray.com\/maven/linkedin.jfrog.io\/artifactory\/open-source/" azkaban-src/build.gradle
# See: https://github.com/azkaban/azkaban/issues/3310/
RUN sed -i.bak "s/download = true/download = false/" azkaban-src/azkaban-web-server/build.gradle
# See: https://github.com/azkaban/azkaban/issues/3329/
RUN sed -i.bak "s/version = rootProject.version/version = '4.0.0'/" azkaban-src/build.gradle
RUN cd azkaban-src && ./gradlew :azkaban-solo-server:installDist
RUN mv /azkaban-src/azkaban-solo-server/build/install/azkaban-solo-server /azkaban

# HBase
RUN wget https://archive.apache.org/dist/hbase/2.4.17/hbase-2.4.17-bin.tar.gz
RUN tar -xvf hbase-2.4.17-bin.tar.gz && mv hbase-2.4.17 hbase
ENV HBASE_HOME=/hbase
ENV PATH=${PATH}:${HBASE_HOME}/bin

# ENTRYPOINT will be defined by Docker Compose
