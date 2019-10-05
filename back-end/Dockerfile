FROM openjdk:10-jre-slim
ENV APP_ROOT /app
RUN mkdir ${APP_ROOT}
WORKDIR ${APP_ROOT}
COPY build/libs/*.jar ${APP_ROOT}/task-manager.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "task-manager.jar"]