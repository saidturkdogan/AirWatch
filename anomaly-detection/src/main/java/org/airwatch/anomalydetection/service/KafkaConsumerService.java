package org.airwatch.anomalydetection.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airwatch.anomalydetection.config.KafkaConfig;
import org.airwatch.anomalydetection.model.AirQualityData;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final AnomalyDetectionService anomalyDetectionService;

    @KafkaListener(topics = KafkaConfig.AIR_QUALITY_TOPIC, groupId = "${spring.kafka.consumer.group-id}", errorHandler = "kafkaErrorHandler")
    public void consumeAirQualityData(AirQualityData airQualityData) {
        try {
            log.info("Received air quality data: {}", airQualityData);
            anomalyDetectionService.detectAnomalies(airQualityData);
        } catch (Exception e) {
            log.error("Error processing air quality data: {}", e.getMessage());
        }
    }
}
