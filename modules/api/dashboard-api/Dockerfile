FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar

COPY ./opentelemetry-javaagent.jar /otelagent/opentelemetry-javaagent.jar

ENV JAVA_TOOL_OPTIONS="-javaagent:/otelagent/opentelemetry-javaagent.jar"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "app.jar"]