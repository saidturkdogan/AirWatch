package org.airwatch.airquality.repository;

import org.airwatch.airquality.model.AirQualityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AirQualityRepository extends JpaRepository<AirQualityData, String> {

    List<AirQualityData> findByLocation(String location);

    List<AirQualityData> findByTimestampBetween(Instant startTime, Instant endTime);

    @Query(value = "SELECT * FROM air_quality_data WHERE location = ?1 ORDER BY timestamp DESC LIMIT ?2", nativeQuery = true)
    List<AirQualityData> findLatestByLocation(String location, int limit);

    @Query(value = "SELECT time_bucket('1 hour', timestamp) as bucket, " +
            "AVG(pm25) as pm25_avg, AVG(pm10) as pm10_avg, AVG(o3) as o3_avg, " +
            "AVG(no2) as no2_avg, AVG(so2) as so2_avg, AVG(co) as co_avg, AVG(aqi) as aqi_avg " +
            "FROM air_quality_data " +
            "WHERE timestamp > NOW() - INTERVAL '24 hours' " +
            "GROUP BY bucket ORDER BY bucket", nativeQuery = true)
    List<Object[]> getHourlyAverages();
}
