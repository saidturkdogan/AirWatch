FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/anomaly-detection-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.kafka.bootstrap-servers=kafka:29092", "--spring.datasource.url=jdbc:postgresql://timescaledb:5432/airwatch?sslmode=disable", "--spring.datasource.username=postgres", "--spring.datasource.password=postgres", "--spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect", "--spring.jpa.hibernate.ddl-auto=update"]
