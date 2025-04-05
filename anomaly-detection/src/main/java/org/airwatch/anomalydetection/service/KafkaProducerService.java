package org.airwatch.anomalydetection.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airwatch.anomalydetection.config.KafkaConfig;
import org.airwatch.anomalydetection.model.Anomaly;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendAnomalyNotification(Anomaly anomaly) {
        log.info("Sending anomaly notification: {}", anomaly);
        kafkaTemplate.send(KafkaConfig.ANOMALY_TOPIC, anomaly.getId(), anomaly);
    }
}
