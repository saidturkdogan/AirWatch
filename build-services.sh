#!/bin/bash
echo "Building all microservices..."

echo "Building API Gateway..."
cd api-gateway
./mvnw clean package -DskipTests
cd ..

echo "Building Air Quality Service..."
cd air-quality
./mvnw clean package -DskipTests
cd ..

echo "Building Anomaly Detection Service..."
cd anomaly-detection
./mvnw clean package -DskipTests
cd ..

echo "Building Notification Service..."
cd notification-service
./mvnw clean package -DskipTests
cd ..

echo "All services built successfully!"
echo "You can now run 'docker-compose up -d' to start all services."
