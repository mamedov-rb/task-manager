#FROM gradle:jdk10 as builder
#COPY ./gradle /usr/src/api
#COPY ./gradlew /usr/src/api/.gradlew
#RUN gradle wrapper && ./gradlew build

FROM openjdk:10-jre-slim
RUN mkdir -p /usr/src/api
WORKDIR /usr/src/api
#COPY ./gradle /usr/src/api
#COPY ./gradlew /usr/src/api/.gradlew
#RUN gradle wrapper && ./gradlew build
COPY ./build/libs/*.jar /usr/src/api
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=development", "/usr/src/api/task-manager-1.0-SNAPSHOT.jar"]
