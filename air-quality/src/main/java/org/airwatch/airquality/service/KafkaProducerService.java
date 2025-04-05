package org.airwatch.airquality.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airwatch.airquality.config.KafkaConfig;
import org.airwatch.airquality.model.AirQualityData;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendAirQualityData(AirQualityData airQualityData) {
        log.info("Sending air quality data to Kafka: {}", airQualityData);
        kafkaTemplate.send(KafkaConfig.AIR_QUALITY_TOPIC, airQualityData.getId(), airQualityData);
    }
}
