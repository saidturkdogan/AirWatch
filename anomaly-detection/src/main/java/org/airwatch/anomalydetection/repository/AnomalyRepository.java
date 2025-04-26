package org.airwatch.anomalydetection.repository;

import org.airwatch.anomalydetection.model.Anomaly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AnomalyRepository extends JpaRepository<Anomaly, String> {

    List<Anomaly> findByLocation(String location);

    List<Anomaly> findByDetectedAtBetween(Instant startTime, Instant endTime);

    List<Anomaly> findBySeverity(String severity);

    List<Anomaly> findByPollutantType(String pollutantType);

    @Query(value = "SELECT * FROM anomalies WHERE location = ?1 ORDER BY detected_at DESC LIMIT ?2", nativeQuery = true)
    List<Anomaly> findLatestByLocation(String location, int limit);

    @Query(value = "SELECT time_bucket('1 hour', detected_at) as bucket, " +
            "COUNT(*) as anomaly_count, " +
            "STRING_AGG(DISTINCT severity, ',') as severities " +
            "FROM anomalies " +
            "WHERE detected_at > NOW() - INTERVAL '24 hours' " +
            "GROUP BY bucket ORDER BY bucket", nativeQuery = true)
    List<Object[]> getHourlyAnomalyCounts();
}
