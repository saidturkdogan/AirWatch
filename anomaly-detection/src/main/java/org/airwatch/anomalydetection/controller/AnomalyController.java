package org.airwatch.anomalydetection.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.airwatch.anomalydetection.model.Anomaly;
import org.airwatch.anomalydetection.service.AnomalyDetectionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/api/anomalies")
@RequiredArgsConstructor
@Tag(name = "Anomaly API", description = "API for retrieving anomaly data")
public class AnomalyController {

    private final AnomalyDetectionService anomalyDetectionService;

    @GetMapping
    @Operation(summary = "Get anomalies by time range", description = "Retrieves all anomalies detected within the specified time range")
    public ResponseEntity<List<Anomaly>> getAnomaliesByTimeRange(
            @Parameter(description = "Start time (ISO format)", example = "2023-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            
            @Parameter(description = "End time (ISO format)", example = "2023-01-02T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        Instant startInstant = startTime.atZone(ZoneId.systemDefault()).toInstant();
        Instant endInstant = endTime.atZone(ZoneId.systemDefault()).toInstant();
        
        List<Anomaly> anomalies = anomalyDetectionService.getAnomaliesByTimeRange(startInstant, endInstant);
        return ResponseEntity.ok(anomalies);
    }
}
