FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
COPY src/main/resources/application-docker.yml /app/application-docker.yml
ENTRYPOINT ["java", "-jar", "app.jar"]