FROM gradle:8.1.1-jdk17 AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY src ./src

RUN gradle clean build -x test

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/Translater-0.0.1-SNAPSHOT.jar /app/translater.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/translater.jar"]
