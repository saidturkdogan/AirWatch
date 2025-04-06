package org.airwatch.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airwatch.notification.config.KafkaConfig;
import org.airwatch.notification.model.Anomaly;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final NotificationService notificationService;

    @KafkaListener(topics = KafkaConfig.ANOMALY_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeAnomalyNotification(Anomaly anomaly) {
        try {
            log.info("Received anomaly notification: {}", anomaly);
            notificationService.processAnomalyNotification(anomaly);
        } catch (Exception e) {
            log.error("Error processing anomaly notification: {}", e.getMessage());
        }
    }
}
