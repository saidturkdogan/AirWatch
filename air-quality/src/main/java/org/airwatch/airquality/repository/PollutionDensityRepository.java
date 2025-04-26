package org.airwatch.airquality.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airwatch.airquality.model.AirQualityData;
import org.airwatch.airquality.model.PollutionDensity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PollutionDensityRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AirQualityRepository airQualityRepository;

    public List<PollutionDensity> getPollutionDensityByRegion() {
        String sql = """
            SELECT 
                location as region,
                AVG(latitude) as latitude,
                AVG(longitude) as longitude,
                AVG(pm25) as pm25_avg,
                AVG(pm10) as pm10_avg,
                AVG(o3) as o3_avg,
                AVG(no2) as no2_avg,
                AVG(so2) as so2_avg,
                AVG(co) as co_avg,
                AVG(aqi) as aqi_avg,
                COUNT(*) as data_point_count
            FROM 
                air_quality_data
            WHERE 
                timestamp > NOW() - INTERVAL '24 hours'
            GROUP BY 
                location
        """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<PollutionDensity> result = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            String region = (String) row.get("region");
            Double aqiAvg = ((Number) row.get("aqi_avg")).doubleValue();
            
            // Determine pollution level
            String level;
            if (aqiAvg > 300) {
                level = "HAZARDOUS";
            } else if (aqiAvg > 200) {
                level = "VERY_UNHEALTHY";
            } else if (aqiAvg > 150) {
                level = "UNHEALTHY";
            } else if (aqiAvg > 100) {
                level = "UNHEALTHY_FOR_SENSITIVE_GROUPS";
            } else if (aqiAvg > 50) {
                level = "MODERATE";
            } else {
                level = "GOOD";
            }
            
            PollutionDensity density = PollutionDensity.builder()
                    .region(region)
                    .latitude(((Number) row.get("latitude")).doubleValue())
                    .longitude(((Number) row.get("longitude")).doubleValue())
                    .radius(10.0) // Arbitrary radius for visualization
                    .pm25Avg(((Number) row.get("pm25_avg")).doubleValue())
                    .pm10Avg(((Number) row.get("pm10_avg")).doubleValue())
                    .o3Avg(((Number) row.get("o3_avg")).doubleValue())
                    .no2Avg(((Number) row.get("no2_avg")).doubleValue())
                    .so2Avg(((Number) row.get("so2_avg")).doubleValue())
                    .coAvg(((Number) row.get("co_avg")).doubleValue())
                    .aqiAvg(aqiAvg)
                    .level(level)
                    .dataPointCount(((Number) row.get("data_point_count")).intValue())
                    .build();
            
            result.add(density);
        }
        
        return result;
    }
}
