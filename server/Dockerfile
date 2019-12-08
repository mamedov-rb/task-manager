FROM gradle:4.10.2-jdk10 as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
USER root
RUN chown -R gradle /home/gradle/src
USER gradle
USER "gradle"
RUN gradle clean build -x test --stacktrace

FROM openjdk:10-jre-slim
RUN mkdir /app
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/task-service.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prodaction", "/app/task-service.jar"]
