# Build with Maven
FROM maven:3-openjdk-17 AS builder

COPY dummy-service /src
WORKDIR /src
RUN mvn clean install

#RUN mvn clean install \
#    -DartifactoryUsr=${ARTIFACTORY_USR} \
#    -DartifactoryPsw=${ARTIFACTORY_PSW} \
#    -Dmaven.test.failure.ignore=true

#FROM builder AS tester
#ARG ARTIFACTORY_USR
#ARG ARTIFACTORY_PSW
#RUN mvn surefire-report:report-only  \
#   -DartifactoryUsr=${ARTIFACTORY_USR} \
#   -DartifactoryPsw=${ARTIFACTORY_PSW}

FROM builder AS publisher

#
# Deploy the compiled JAR file to a plain JRE image
#
FROM amazoncorretto:17 AS production
#FROM dig-grid-artifactory.apps.ge.com/base-images-docker/java-jre-corretto:8.4-208.11.0.12.7.1 AS production
#USER root
#RUN microdnf install shadow-utils && \
#    groupadd --gid 1000 ge && \
#    useradd -M --uid 1000 --gid 1000 --home /usr/share/ge ge && \
#    microdnf remove shadow-utils

WORKDIR /app
COPY --from=Builder /src/target/dummy-service-*.jar ./dummy-service.jar
COPY dummy-service/src/main/resources/application.yaml config/

ENV JVM_OPTS ""
EXPOSE 8282 8282
#USER ge

ENTRYPOINT java $JVM_OPTS -jar dummy-service.jar --spring.config.location=/app/config/

