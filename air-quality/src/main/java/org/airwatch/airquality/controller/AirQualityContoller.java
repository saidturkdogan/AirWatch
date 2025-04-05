package org.airwatch.airquality.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.airwatch.airquality.model.AirQualityData;
import org.airwatch.airquality.model.PollutionDensity;
import org.airwatch.airquality.service.AirQualityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Air Quality API", description = "API for retrieving air quality data")
public class AirQualityContoller {

    private final AirQualityService airQualityService;

    @GetMapping("/air-quality")
    @Operation(summary = "Get air quality data by location", description = "Retrieves air quality data for a specific location")
    public ResponseEntity<List<AirQualityData>> getAirQualityByLocation(
            @Parameter(description = "Location name", example = "Istanbul")
            @RequestParam String location) {
        List<AirQualityData> data = airQualityService.getAirQualityByLocation(location);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/pollution-density")
    @Operation(summary = "Get pollution density by region", description = "Retrieves pollution density data aggregated by geographic region")
    public ResponseEntity<List<PollutionDensity>> getPollutionDensityByRegion() {
        List<PollutionDensity> data = airQualityService.getPollutionDensityByRegion();
        return ResponseEntity.ok(data);
    }
}
