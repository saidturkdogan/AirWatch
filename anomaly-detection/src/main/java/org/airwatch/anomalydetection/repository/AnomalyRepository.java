package org.airwatch.anomalydetection.repository;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airwatch.anomalydetection.model.Anomaly;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class AnomalyRepository {

    private final InfluxDBClient influxDBClient;

    // In-memory storage for anomalies (for development/testing and as a fallback)
    private final ConcurrentHashMap<String, Anomaly> anomalies = new ConcurrentHashMap<>();

    public Anomaly save(Anomaly anomaly) {
        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            // Convert to InfluxDB Point
            com.influxdb.client.write.Point point = com.influxdb.client.write.Point.measurement("anomalies")
                    .addTag("id", anomaly.getId())
                    .addTag("location", anomaly.getLocation())
                    .addTag("pollutantType", anomaly.getPollutantType())
                    .addTag("severity", anomaly.getSeverity())
                    .addField("latitude", anomaly.getLatitude())
                    .addField("longitude", anomaly.getLongitude())
                    .addField("value", anomaly.getValue())
                    .addField("threshold", anomaly.getThreshold())
                    .addField("description", anomaly.getDescription())
                    .time(anomaly.getDetectedAt(), WritePrecision.NS);

            // Write to InfluxDB
            writeApi.writePoint(point);

            // Also store in memory for development/testing
            anomalies.put(anomaly.getId(), anomaly);

            log.info("Saved anomaly: {}", anomaly);
        } catch (Exception e) {
            log.error("Error saving anomaly to InfluxDB", e);
            // Still store in memory even if InfluxDB fails
            anomalies.put(anomaly.getId(), anomaly);
        }
        return anomaly;
    }

    public List<Anomaly> findAll() {
        try {
            QueryApi queryApi = influxDBClient.getQueryApi();
            String flux = "from(bucket: \"anomaly-detection\") " +
                    "  |> range(start: -30d) " +
                    "  |> filter(fn: (r) => r._measurement == \"anomalies\") " +
                    "  |> pivot(rowKey:[\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")";

            List<FluxTable> tables = queryApi.query(flux);
            List<Anomaly> result = new ArrayList<>();

            for (FluxTable table : tables) {
                for (FluxRecord record : table.getRecords()) {
                    try {
                        Anomaly anomaly = new Anomaly();
                        anomaly.setId(record.getValueByKey("id").toString());
                        anomaly.setLocation(record.getValueByKey("location").toString());
                        anomaly.setPollutantType(record.getValueByKey("pollutantType").toString());
                        anomaly.setSeverity(record.getValueByKey("severity").toString());
                        anomaly.setLatitude(Double.parseDouble(record.getValueByKey("latitude").toString()));
                        anomaly.setLongitude(Double.parseDouble(record.getValueByKey("longitude").toString()));
                        anomaly.setValue(Double.parseDouble(record.getValueByKey("value").toString()));
                        anomaly.setThreshold(Double.parseDouble(record.getValueByKey("threshold").toString()));
                        anomaly.setDescription(record.getValueByKey("description").toString());
                        anomaly.setDetectedAt(Instant.parse(record.getTime().toString()));
                        result.add(anomaly);
                    } catch (Exception e) {
                        log.error("Error parsing anomaly record from InfluxDB", e);
                    }
                }
            }

            return result;
        } catch (Exception e) {
            log.error("Error querying InfluxDB", e);
            // Fall back to in-memory cache
            return new ArrayList<>(anomalies.values());
        }
    }

    public Anomaly findById(String id) {
        try {
            QueryApi queryApi = influxDBClient.getQueryApi();
            String flux = String.format(
                    "from(bucket: \"anomaly-detection\") " +
                    "  |> range(start: -30d) " +
                    "  |> filter(fn: (r) => r._measurement == \"anomalies\" and r.id == \"%s\") " +
                    "  |> pivot(rowKey:[\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")",
                    id);

            List<FluxTable> tables = queryApi.query(flux);
            if (tables.isEmpty() || tables.get(0).getRecords().isEmpty()) {
                return anomalies.get(id); // Fall back to in-memory cache
            }

            FluxRecord record = tables.get(0).getRecords().get(0);
            Anomaly anomaly = new Anomaly();
            anomaly.setId(record.getValueByKey("id").toString());
            anomaly.setLocation(record.getValueByKey("location").toString());
            anomaly.setPollutantType(record.getValueByKey("pollutantType").toString());
            anomaly.setSeverity(record.getValueByKey("severity").toString());
            anomaly.setLatitude(Double.parseDouble(record.getValueByKey("latitude").toString()));
            anomaly.setLongitude(Double.parseDouble(record.getValueByKey("longitude").toString()));
            anomaly.setValue(Double.parseDouble(record.getValueByKey("value").toString()));
            anomaly.setThreshold(Double.parseDouble(record.getValueByKey("threshold").toString()));
            anomaly.setDescription(record.getValueByKey("description").toString());
            anomaly.setDetectedAt(Instant.parse(record.getTime().toString()));
            return anomaly;
        } catch (Exception e) {
            log.error("Error querying InfluxDB for anomaly with id: {}", id, e);
            // Fall back to in-memory cache
            return anomalies.get(id);
        }
    }

    public List<Anomaly> findByDetectedAtBetween(Instant startTime, Instant endTime) {
        try {
            QueryApi queryApi = influxDBClient.getQueryApi();
            String flux = String.format(
                    "from(bucket: \"anomaly-detection\") " +
                    "  |> range(start: %s, stop: %s) " +
                    "  |> filter(fn: (r) => r._measurement == \"anomalies\") " +
                    "  |> pivot(rowKey:[\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")",
                    startTime.toString(), endTime.toString());

            List<FluxTable> tables = queryApi.query(flux);
            List<Anomaly> result = new ArrayList<>();

            for (FluxTable table : tables) {
                for (FluxRecord record : table.getRecords()) {
                    try {
                        Anomaly anomaly = new Anomaly();
                        anomaly.setId(record.getValueByKey("id").toString());
                        anomaly.setLocation(record.getValueByKey("location").toString());
                        anomaly.setPollutantType(record.getValueByKey("pollutantType").toString());
                        anomaly.setSeverity(record.getValueByKey("severity").toString());
                        anomaly.setLatitude(Double.parseDouble(record.getValueByKey("latitude").toString()));
                        anomaly.setLongitude(Double.parseDouble(record.getValueByKey("longitude").toString()));
                        anomaly.setValue(Double.parseDouble(record.getValueByKey("value").toString()));
                        anomaly.setThreshold(Double.parseDouble(record.getValueByKey("threshold").toString()));
                        anomaly.setDescription(record.getValueByKey("description").toString());
                        anomaly.setDetectedAt(Instant.parse(record.getTime().toString()));
                        result.add(anomaly);
                    } catch (Exception e) {
                        log.error("Error parsing anomaly record from InfluxDB", e);
                    }
                }
            }

            return result;
        } catch (Exception e) {
            log.error("Error querying InfluxDB for anomalies between {} and {}", startTime, endTime, e);
            // Fall back to in-memory cache
            return anomalies.values().stream()
                    .filter(anomaly -> !anomaly.getDetectedAt().isBefore(startTime) && !anomaly.getDetectedAt().isAfter(endTime))
                    .collect(Collectors.toList());
        }
    }
}
