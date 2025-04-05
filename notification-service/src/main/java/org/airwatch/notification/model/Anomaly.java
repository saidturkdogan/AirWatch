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
public class Anomaly {
    private String id;
    private String location;
    private Double latitude;
    private Double longitude;
    private Instant detectedAt;
    private String pollutantType;
    private Double value;
    private Double threshold;
    private String severity;
    private String description;
}
