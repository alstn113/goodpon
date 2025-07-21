FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "app.jar"]