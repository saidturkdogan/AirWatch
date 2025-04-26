package org.airwatch.anomalydetection.model;

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
@Table(name = "anomalies")
public class Anomaly {
    @Id
    private String id;
    private String location;
    private Double latitude;
    private Double longitude;
    @Column(columnDefinition = "TIMESTAMPTZ")
    private Instant detectedAt;
    private String pollutantType;
    private Double value;
    private Double threshold;
    private String severity;
    private String description;
}
