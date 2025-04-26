package org.airwatch.airquality.model;

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
@Table(name = "air_quality_data")
public class AirQualityData {
    @Id
    private String id;
    private Double latitude;
    private Double longitude;
    private String location;
    @Column(columnDefinition = "TIMESTAMPTZ")
    private Instant timestamp;
    private Double pm25;
    private Double pm10;
    private Double o3;
    private Double no2;
    private Double so2;
    private Double co;
    private Double aqi;
    private String source;
}
