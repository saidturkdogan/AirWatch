package org.airwatch.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private String id;
    private String type;
    private String message;
    private String severity;
    private Instant timestamp;
    private boolean read;
    private String source;
}
