FROM openjdk:10-jre-slim
RUN mkdir -p /usr/src/api
WORKDIR /usr/src/api
COPY ./build/libs/*.jar /usr/src/api
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=development", "/usr/src/api/task-manager-1.0-SNAPSHOT.jar"]
