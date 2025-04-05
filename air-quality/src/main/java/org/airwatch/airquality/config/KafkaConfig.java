package org.airwatch.airquality.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String AIR_QUALITY_TOPIC = "air-quality-data";

    @Bean
    public NewTopic airQualityTopic() {
        return TopicBuilder.name(AIR_QUALITY_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
