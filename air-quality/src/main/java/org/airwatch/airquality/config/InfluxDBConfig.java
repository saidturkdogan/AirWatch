package org.airwatch.airquality.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class InfluxDBConfig {

    @Value("${influxdb.url}")
    private String url;

    @Value("${influxdb.token}")
    private String token;

    @Value("${influxdb.org}")
    private String org;

    @Value("${influxdb.bucket}")
    private String bucket;

    @Bean
    public InfluxDBClient influxDBClient() {
        try {
            log.info("Connecting to InfluxDB at {}", url);
            return InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
        } catch (Exception e) {
            log.error("Failed to connect to InfluxDB: {}", e.getMessage());
            log.warn("Using mock InfluxDB client");
            // Return a mock client that does nothing
            return new MockInfluxDBClient();
        }
    }
}
