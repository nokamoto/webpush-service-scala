FROM openjdk:8u181-jre-slim

ARG VERSION
ARG APP=webpush-service-scala

COPY target/universal/${APP}-${VERSION}.tgz .

RUN tar -zxvf ${APP}-${VERSION}.tgz && mv ${APP}-${VERSION} ${APP}

RUN rm ${APP}-${VERSION}.tgz

ENTRYPOINT [ "webpush-service-scala/bin/webpush-service-scala" ]
