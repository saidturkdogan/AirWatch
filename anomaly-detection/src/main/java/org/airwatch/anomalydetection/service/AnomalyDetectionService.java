package org.airwatch.anomalydetection.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airwatch.anomalydetection.model.AirQualityData;
import org.airwatch.anomalydetection.model.Anomaly;
import org.airwatch.anomalydetection.repository.AnomalyRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnomalyDetectionService {

    private final AnomalyRepository anomalyRepository;
    private final KafkaProducerService kafkaProducerService;

    // Thresholds for different pollutants
    private static final double PM25_THRESHOLD = 35.0;
    private static final double PM10_THRESHOLD = 150.0;
    private static final double O3_THRESHOLD = 0.070;
    private static final double NO2_THRESHOLD = 100.0;
    private static final double SO2_THRESHOLD = 75.0;
    private static final double CO_THRESHOLD = 9.0;

    public void detectAnomalies(AirQualityData data) {
        log.info("Detecting anomalies for data: {}", data);
        
        // Check for PM2.5 anomaly
        if (data.getPm25() != null && data.getPm25() > PM25_THRESHOLD) {
            createAndSendAnomaly(data, "PM2.5", data.getPm25(), PM25_THRESHOLD);
        }
        
        // Check for PM10 anomaly
        if (data.getPm10() != null && data.getPm10() > PM10_THRESHOLD) {
            createAndSendAnomaly(data, "PM10", data.getPm10(), PM10_THRESHOLD);
        }
        
        // Check for O3 anomaly
        if (data.getO3() != null && data.getO3() > O3_THRESHOLD) {
            createAndSendAnomaly(data, "O3", data.getO3(), O3_THRESHOLD);
        }
        
        // Check for NO2 anomaly
        if (data.getNo2() != null && data.getNo2() > NO2_THRESHOLD) {
            createAndSendAnomaly(data, "NO2", data.getNo2(), NO2_THRESHOLD);
        }
        
        // Check for SO2 anomaly
        if (data.getSo2() != null && data.getSo2() > SO2_THRESHOLD) {
            createAndSendAnomaly(data, "SO2", data.getSo2(), SO2_THRESHOLD);
        }
        
        // Check for CO anomaly
        if (data.getCo() != null && data.getCo() > CO_THRESHOLD) {
            createAndSendAnomaly(data, "CO", data.getCo(), CO_THRESHOLD);
        }
    }

    private void createAndSendAnomaly(AirQualityData data, String pollutantType, Double value, Double threshold) {
        String severity = calculateSeverity(value, threshold);
        
        Anomaly anomaly = Anomaly.builder()
                .id(UUID.randomUUID().toString())
                .location(data.getLocation())
                .latitude(data.getLatitude())
                .longitude(data.getLongitude())
                .detectedAt(Instant.now())
                .pollutantType(pollutantType)
                .value(value)
                .threshold(threshold)
                .severity(severity)
                .description(String.format("%s level of %s detected at %s, exceeding threshold of %s", 
                        severity, pollutantType, data.getLocation(), threshold))
                .build();
        
        // Save to repository
        anomalyRepository.save(anomaly);
        
        // Send notification via Kafka
        kafkaProducerService.sendAnomalyNotification(anomaly);
    }

    private String calculateSeverity(Double value, Double threshold) {
        double ratio = value / threshold;
        
        if (ratio >= 2.0) {
            return "SEVERE";
        } else if (ratio >= 1.5) {
            return "HIGH";
        } else {
            return "MODERATE";
        }
    }

    public List<Anomaly> getAnomaliesByTimeRange(Instant startTime, Instant endTime) {
        return anomalyRepository.findByDetectedAtBetween(startTime, endTime);
    }
}
