#FROM gradle:jdk10 as builder
#COPY ./gradle /usr/src/api
#COPY ./gradlew /usr/src/api/.gradlew
#RUN gradle wrapper && ./gradlew build

FROM openjdk:10-jre-slim
WORKDIR /app
#COPY ./gradle /usr/src/api
#COPY ./gradlew /usr/src/api/.gradlew
#RUN gradle wrapper && ./gradlew build
COPY ./build/libs/*.jar /app
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=development", "/app/task-manager-1.0-SNAPSHOT.jar"]
