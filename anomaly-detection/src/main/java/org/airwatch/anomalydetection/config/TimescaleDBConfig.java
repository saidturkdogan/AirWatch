package org.airwatch.anomalydetection.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class TimescaleDBConfig {

    private final JdbcTemplate jdbcTemplate;

    @EventListener(ContextRefreshedEvent.class)
    public void createHypertable() {
        try {
            // First, check if the TimescaleDB extension is installed
            jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS timescaledb CASCADE");
            
            // Create hypertable for anomalies
            jdbcTemplate.execute(
                "SELECT create_hypertable('anomalies', 'detected_at', if_not_exists => TRUE)"
            );
            
            log.info("TimescaleDB hypertable created successfully for anomalies");
        } catch (Exception e) {
            log.error("Error creating TimescaleDB hypertable: {}", e.getMessage(), e);
        }
    }
}
