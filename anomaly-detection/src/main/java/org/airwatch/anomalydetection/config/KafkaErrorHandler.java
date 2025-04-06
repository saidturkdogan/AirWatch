package org.airwatch.anomalydetection.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaErrorHandler implements KafkaListenerErrorHandler {

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        log.error("Error in Kafka consumer: {}", exception.getMessage());
        log.debug("Error details:", exception);
        log.debug("Problematic message: {}", message);
        // Return null to indicate the error has been handled
        return null;
    }
}
