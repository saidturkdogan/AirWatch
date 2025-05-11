@echo off
echo Building all microservices...

echo Building API Gateway...
cd api-gateway
call mvnw clean package -DskipTests
cd ..

echo Building Air Quality Service...
cd air-quality
call mvnw clean package -DskipTests
cd ..

echo Building Anomaly Detection Service...
cd anomaly-detection
call mvnw clean package -DskipTests
cd ..

echo Building Notification Service...
cd notification-service
call mvnw clean package -DskipTests
cd ..

echo All services built successfully!
echo You can now run 'docker-compose up -d' to start all services.
