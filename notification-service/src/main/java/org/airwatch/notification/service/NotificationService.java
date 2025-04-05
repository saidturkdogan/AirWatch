package org.airwatch.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airwatch.notification.model.Anomaly;
import org.airwatch.notification.model.Notification;
import org.airwatch.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void processAnomalyNotification(Anomaly anomaly) {
        log.info("Processing anomaly notification: {}", anomaly);
        
        // Create notification from anomaly
        Notification notification = Notification.builder()
                .id(UUID.randomUUID().toString())
                .type("ANOMALY")
                .message(anomaly.getDescription())
                .severity(anomaly.getSeverity())
                .timestamp(Instant.now())
                .read(false)
                .source("anomaly-detection-service")
                .build();
        
        // Save notification
        notificationRepository.save(notification);
        
        // In a real application, this would send an email, SMS, or push notification
        sendNotification(notification);
    }
    
    private void sendNotification(Notification notification) {
        // This is a placeholder for actual notification sending logic
        // In a real application, this would integrate with email, SMS, or push notification services
        log.info("Sending notification: {}", notification);
        log.info("ALERT: {} - {}", notification.getSeverity(), notification.getMessage());
    }
    
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
    
    public List<Notification> getUnreadNotifications() {
        return notificationRepository.findByRead(false);
    }
    
    public Notification markAsRead(String id) {
        Notification notification = notificationRepository.findById(id);
        if (notification != null) {
            notification.setRead(true);
            return notificationRepository.save(notification);
        }
        return null;
    }
}
