FROM oraclelinux:9

# For testing use:
#   $ docker run --interactive --tty --rm --entrypoint bash oraclelinux:9

# ----------------------------------------------------------------------------------------------------------------------
# Java LTS.
# ----------------------------------------------------------------------------------------------------------------------
RUN dnf -y install java-1.8.0-openjdk java-1.8.0-openjdk-devel
ENV JAVA_HOME=/usr/lib/jvm/java

# ----------------------------------------------------------------------------------------------------------------------
# Git.
# ----------------------------------------------------------------------------------------------------------------------
RUN dnf -y install git

# ----------------------------------------------------------------------------------------------------------------------
# Node.js.
# ----------------------------------------------------------------------------------------------------------------------
RUN dnf -y install nodejs npm

# ----------------------------------------------------------------------------------------------------------------------
# Build.
# ----------------------------------------------------------------------------------------------------------------------
ARG AZKABAN_VER=4.0.0
ARG AZKABAN_DISTR="azkaban-${AZKABAN_VER}"
ARG AZKABAN_ARCH="${AZKABAN_DISTR}-src.tar.gz"
RUN curl -L -o "${AZKABAN_ARCH}" "https://github.com/azkaban/azkaban/archive/refs/tags/${AZKABAN_VER}.tar.gz"
RUN tar -xf "${AZKABAN_ARCH}"

# See:
#   https://github.com/azkaban/azkaban/issues/2899/
RUN sed -i.bak "s/linkedin.bintray.com\/maven/linkedin.jfrog.io\/artifactory\/open-source/" "/${AZKABAN_DISTR}/build.gradle"

# See:
#   https://github.com/azkaban/azkaban/issues/3310/
RUN sed -i.bak "s/download = true/download = false/" "/${AZKABAN_DISTR}/azkaban-web-server/build.gradle"

# See:
#   https://github.com/azkaban/azkaban/issues/3329/
RUN sed -i.bak "s/version = rootProject.version/version = '${AZKABAN_VER}'/" "/${AZKABAN_DISTR}/build.gradle"

RUN cd "/${AZKABAN_DISTR}" \
    && ./gradlew :azkaban-solo-server:installDist

RUN mv "/${AZKABAN_DISTR}/azkaban-solo-server/build/install/azkaban-solo-server" /azkaban
RUN tar -czf ${AZKABAN_DISTR}.tar.gz /azkaban

ENTRYPOINT ["tail", "-f", "/dev/null"]
