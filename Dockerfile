FROM amazoncorretto:11

## TODO: add non root mode
# RUN yum install shadow-utils.x86_64 -y
# RUN addgroup -S spring && adduser -S spring -G spring
# USER spring:spring

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]


