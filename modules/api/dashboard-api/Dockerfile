FROM eclipse-temurin:21-jdk AS agent-stage

WORKDIR /otelagent

ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.18.1/opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar

FROM eclipse-temurin:21-jdk AS app-stage

WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

COPY --from=agent-stage /otelagent ./otelagent

ENTRYPOINT ["java", "-javaagent:./otelagent/opentelemetry-javaagent.jar", "-jar", "app.jar"]
