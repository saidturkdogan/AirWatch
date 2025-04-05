package org.airwatch.notification.repository;

import org.airwatch.notification.model.Notification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class NotificationRepository {
    
    // In-memory storage for notifications (in a real application, this would use a database)
    private final ConcurrentHashMap<String, Notification> notifications = new ConcurrentHashMap<>();
    
    public Notification save(Notification notification) {
        notifications.put(notification.getId(), notification);
        return notification;
    }
    
    public List<Notification> findAll() {
        return new ArrayList<>(notifications.values());
    }
    
    public Notification findById(String id) {
        return notifications.get(id);
    }
    
    public List<Notification> findByRead(boolean read) {
        return notifications.values().stream()
                .filter(notification -> notification.isRead() == read)
                .collect(Collectors.toList());
    }
}
