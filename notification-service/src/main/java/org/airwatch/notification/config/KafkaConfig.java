package org.airwatch.notification.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@Slf4j
public class KafkaConfig {

    public static final String ANOMALY_TOPIC = "anomaly-notifications";

    @Bean
    public NewTopic anomalyTopic() {
        return TopicBuilder.name(ANOMALY_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            ConsumerFactory<Object, Object> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        // Configure error handling
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                (record, exception) -> {
                    log.error("Error in Kafka consumer: {}", exception.getMessage());
                    log.debug("Error details:", exception);
                    log.debug("Failed record: {}", record);
                },
                // Retry 3 times, with 1 second between retries
                new FixedBackOff(1000L, 3)
        );

        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}
