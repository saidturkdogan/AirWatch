<p align="center">
  <img src="https://i.hizliresim.com/8n5tsw9.png" alt="AirWatch Logo" width="600">
</p>

## AirWatch - Air Quality Monitoring System

AirWatch is a microservice-based system designed to collect, analyze, and detect anomalies in air quality data. This system monitors air quality data from various locations, detects anomalies, and sends notifications when necessary.

### System Architecture

AirWatch consists of the following microservices:

1. **API Gateway**: Entry point for all client requests, routes requests to appropriate services.
2. **Air Quality Service**: Collects and provides air quality data.
3. **Anomaly Detection Service**: Analyzes air quality data to detect anomalies.
4. **Notification Service**: Creates and manages notifications for detected anomalies.

### Technology Stack

- **Spring Boot**: For microservice development
- **Spring Cloud Gateway**: For API Gateway
- **Kafka**: For asynchronous communication between microservices
- **InfluxDB**: For storing time-series data
- **Swagger/OpenAPI**: For API documentation
- **Docker**: For containerization

### Getting Started

#### Prerequisites

- Java 17 or higher
- Maven
- Docker and Docker Compose
- Git

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/AirWatch.git
   cd AirWatch
   ```

2. Start the required services (Kafka, Zookeeper, InfluxDB) with Docker:
   ```bash
   docker-compose up -d
   ```

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Start the microservices:
   ```bash
   # Start each service in a separate terminal window
   mvn spring-boot:run -pl api-gateway
   mvn spring-boot:run -pl air-quality
   mvn spring-boot:run -pl anomaly-detection
   mvn spring-boot:run -pl notification-service
   ```

### API Access

After the system is started, you can access the APIs via the following URLs:

- **API Gateway**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html

## API Endpoints

### Air Quality Service

- `GET /api/air-quality?location={location}`: Retrieves air quality data for a specific location.
- `GET /api/pollution-density`: Retrieves pollution density data by geographical regions.

### Anomaly Detection Service

- `GET /api/anomalies?startTime={startTime}&endTime={endTime}`: Lists anomalies detected within a specific time range.

### Notification Service

- `GET /api/notifications`: Lists all notifications.
- `GET /api/notifications/{id}`: Retrieves details of a specific notification.

## Data Flow

1. Air Quality Service collects air quality data and stores it in InfluxDB.
2. Air Quality Service sends raw data to Kafka.
3. Anomaly Detection Service consumes data from Kafka, analyzes it, and detects anomalies.
4. Detected anomalies are stored by the Anomaly Detection Service and sent to Kafka.
5. Notification Service consumes anomaly notifications from Kafka and processes them.

## Development

### Adding a New Microservice

1. Create a new Maven module:
   ```bash
   mvn archetype:generate -DgroupId=org.airwatch -DartifactId=new-service -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
   ```

2. Add the new module to the parent POM:
   ```xml
   <modules>
       <!-- Existing modules -->
       <module>new-service</module>
   </modules>
   ```

3. Create Spring Boot configuration for the new service.

4. Add routing rules for the new service in the API Gateway.

### Code Style

This project follows the Google Java Style Guide. To check the code style:

```bash
mvn checkstyle:check
```

## Troubleshooting

### Kafka Connection Issues

If you encounter the "Connection to node -1 could not be established" error:

1. Make sure Kafka is running:
   ```bash
   docker ps | grep kafka
   ```

2. Check the Kafka configuration:
   ```properties
   spring.kafka.bootstrap-servers=localhost:9092
   ```

### InfluxDB Connection Issues

For InfluxDB connection issues:

1. Make sure InfluxDB is running:
   ```bash
   docker ps | grep influxdb
   ```

2. Check the InfluxDB configuration:
   ```properties
   influxdb.url=http://localhost:8086
   influxdb.token=my-super-secret-token
   influxdb.org=airwatch
   influxdb.bucket=air-quality
   ```

## Contributing

1. Fork this repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push your branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
