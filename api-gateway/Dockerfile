FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/api-gateway-*.jar app.jar
EXPOSE 8484
ENTRYPOINT ["java", "-jar", "app.jar"]
