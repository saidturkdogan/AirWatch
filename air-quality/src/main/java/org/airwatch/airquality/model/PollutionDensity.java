package org.airwatch.airquality.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PollutionDensity {
    private String region;
    private Double latitude;
    private Double longitude;
    private Double radius;
    private Double pm25Avg;
    private Double pm10Avg;
    private Double o3Avg;
    private Double no2Avg;
    private Double so2Avg;
    private Double coAvg;
    private Double aqiAvg;
    private String level;
    private Integer dataPointCount;
}
