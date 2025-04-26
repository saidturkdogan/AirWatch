package org.airwatch.notification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    private String id;
    private String type;
    private String message;
    private String severity;
    @Column(columnDefinition = "TIMESTAMPTZ")
    private Instant timestamp;
    private boolean read;
    private String source;
}
