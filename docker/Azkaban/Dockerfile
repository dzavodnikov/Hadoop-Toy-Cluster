FROM oraclelinux:8

# For testing use:
#   % docker run --rm -it --entrypoint bash oraclelinux:8

RUN yum -y install wget hostname jq

RUN yum -y install java-1.8.0-openjdk-devel
ENV JAVA_HOME=/usr/lib/jvm/jre

RUN yum -y install git nodejs npm

ARG AZKABAN_VER=4.0.0
ARG AZKABAN_DISTR=azkaban-${AZKABAN_VER}
ARG AZKABAN_ARCH=${AZKABAN_DISTR}-src.tar.gz

RUN wget https://github.com/azkaban/azkaban/archive/refs/tags/${AZKABAN_VER}.tar.gz -O ${AZKABAN_ARCH}
RUN tar -xf ${AZKABAN_ARCH}

# See: https://github.com/azkaban/azkaban/issues/2899/
RUN sed -i.bak "s/linkedin.bintray.com\/maven/linkedin.jfrog.io\/artifactory\/open-source/" ${AZKABAN_DISTR}/build.gradle

# See: https://github.com/azkaban/azkaban/issues/3310/
RUN sed -i.bak "s/download = true/download = false/" ${AZKABAN_DISTR}/azkaban-web-server/build.gradle

# See: https://github.com/azkaban/azkaban/issues/3329/
RUN sed -i.bak "s/version = rootProject.version/version = '${AZKABAN_VER}'/" ${AZKABAN_DISTR}/build.gradle

RUN cd /${AZKABAN_DISTR} && ./gradlew :azkaban-solo-server:installDist

RUN mv /${AZKABAN_DISTR}/azkaban-solo-server/build/install/azkaban-solo-server /azkaban
RUN tar -czf ${AZKABAN_DISTR}.tar.gz /azkaban

ENTRYPOINT ["tail", "-f", "/dev/null"]
