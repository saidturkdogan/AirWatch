package org.airwatch.airquality.repository;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airwatch.airquality.model.AirQualityData;
import org.airwatch.airquality.model.PollutionDensity;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class AirQualityRepository {

    private final InfluxDBClient influxDBClient;

    // For development/testing purposes, we'll also keep an in-memory cache
    private final Map<String, AirQualityData> inMemoryCache = new HashMap<>();

    public void save(AirQualityData data) {
        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            // Convert to InfluxDB Point
            com.influxdb.client.write.Point point = com.influxdb.client.write.Point.measurement("air_quality")
                    .addTag("id", data.getId())
                    .addTag("location", data.getLocation())
                    .addTag("source", data.getSource())
                    .addField("latitude", data.getLatitude())
                    .addField("longitude", data.getLongitude())
                    .addField("pm25", data.getPm25())
                    .addField("pm10", data.getPm10())
                    .addField("o3", data.getO3())
                    .addField("no2", data.getNo2())
                    .addField("so2", data.getSo2())
                    .addField("co", data.getCo())
                    .addField("aqi", data.getAqi())
                    .time(data.getTimestamp(), WritePrecision.NS);

            // Write to InfluxDB
            writeApi.writePoint(point);
            
            // Also store in memory for development/testing
            inMemoryCache.put(data.getId(), data);
            
            log.info("Saved air quality data: {}", data);
        } catch (Exception e) {
            log.error("Error saving air quality data to InfluxDB", e);
            // Still store in memory even if InfluxDB fails
            inMemoryCache.put(data.getId(), data);
        }
    }

    public List<AirQualityData> findByLocation(String location) {
        try {
            QueryApi queryApi = influxDBClient.getQueryApi();
            String flux = String.format(
                    "from(bucket: \"air-quality\") " +
                    "  |> range(start: -30d) " +
                    "  |> filter(fn: (r) => r._measurement == \"air_quality\" and r.location == \"%s\") " +
                    "  |> pivot(rowKey:[\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")",
                    location);

            List<FluxTable> tables = queryApi.query(flux);
            List<AirQualityData> result = new ArrayList<>();

            for (FluxTable table : tables) {
                for (FluxRecord record : table.getRecords()) {
                    // Convert FluxRecord to AirQualityData
                    // This is simplified and would need to be expanded in a real application
                    AirQualityData data = new AirQualityData();
                    data.setId(record.getValueByKey("id").toString());
                    data.setLocation(record.getValueByKey("location").toString());
                    data.setTimestamp(Instant.parse(record.getTime().toString()));
                    // ... set other fields
                    result.add(data);
                }
            }

            return result;
        } catch (Exception e) {
            log.error("Error querying InfluxDB", e);
            // Fall back to in-memory cache
            return inMemoryCache.values().stream()
                    .filter(data -> location.equals(data.getLocation()))
                    .collect(Collectors.toList());
        }
    }

    public List<AirQualityData> findAll() {
        // For simplicity, just return the in-memory cache
        return new ArrayList<>(inMemoryCache.values());
    }

    public List<PollutionDensity> getPollutionDensityByRegion() {
        // This would normally query InfluxDB with geographic aggregations
        // For simplicity, we'll just return some mock data based on our in-memory cache
        Map<String, List<AirQualityData>> dataByRegion = inMemoryCache.values().stream()
                .collect(Collectors.groupingBy(AirQualityData::getLocation));
        
        List<PollutionDensity> result = new ArrayList<>();
        
        for (Map.Entry<String, List<AirQualityData>> entry : dataByRegion.entrySet()) {
            String region = entry.getKey();
            List<AirQualityData> regionData = entry.getValue();
            
            if (regionData.isEmpty()) {
                continue;
            }
            
            // Calculate averages
            double pm25Avg = regionData.stream()
                    .filter(d -> d.getPm25() != null)
                    .mapToDouble(AirQualityData::getPm25)
                    .average()
                    .orElse(0);
            
            double pm10Avg = regionData.stream()
                    .filter(d -> d.getPm10() != null)
                    .mapToDouble(AirQualityData::getPm10)
                    .average()
                    .orElse(0);
            
            double o3Avg = regionData.stream()
                    .filter(d -> d.getO3() != null)
                    .mapToDouble(AirQualityData::getO3)
                    .average()
                    .orElse(0);
            
            double no2Avg = regionData.stream()
                    .filter(d -> d.getNo2() != null)
                    .mapToDouble(AirQualityData::getNo2)
                    .average()
                    .orElse(0);
            
            double so2Avg = regionData.stream()
                    .filter(d -> d.getSo2() != null)
                    .mapToDouble(AirQualityData::getSo2)
                    .average()
                    .orElse(0);
            
            double coAvg = regionData.stream()
                    .filter(d -> d.getCo() != null)
                    .mapToDouble(AirQualityData::getCo)
                    .average()
                    .orElse(0);
            
            double aqiAvg = regionData.stream()
                    .filter(d -> d.getAqi() != null)
                    .mapToDouble(AirQualityData::getAqi)
                    .average()
                    .orElse(0);
            
            // Get a representative lat/long for the region
            AirQualityData representative = regionData.get(0);
            
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
                    .latitude(representative.getLatitude())
                    .longitude(representative.getLongitude())
                    .radius(10.0) // Arbitrary radius for visualization
                    .pm25Avg(pm25Avg)
                    .pm10Avg(pm10Avg)
                    .o3Avg(o3Avg)
                    .no2Avg(no2Avg)
                    .so2Avg(so2Avg)
                    .coAvg(coAvg)
                    .aqiAvg(aqiAvg)
                    .level(level)
                    .dataPointCount(regionData.size())
                    .build();
            
            result.add(density);
        }
        
        return result;
    }
}
