package org.airwatch.anomalydetection.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirQualityData {
    private String id;
    private Double latitude;
    private Double longitude;
    private String location;
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
