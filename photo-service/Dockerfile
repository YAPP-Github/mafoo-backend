FROM amazoncorretto:17-alpine3.17-jdk

RUN apt-get update && \
    apt-get install -y ffmpeg && \
    apt-get clean

RUN mkdir -p /usr/bin/recap/tmp && \
    wget -O /tmp/src.tar.gz $SRC_URL && \
    tar -xvzf /tmp/src.tar.gz -C /usr/bin/recap && \
    rm /tmp/src.tar.gz
