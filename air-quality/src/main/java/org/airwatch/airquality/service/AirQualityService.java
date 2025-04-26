package org.airwatch.airquality.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.airwatch.airquality.model.AirQualityData;
import org.airwatch.airquality.model.PollutionDensity;
import org.airwatch.airquality.repository.AirQualityRepository;
import org.airwatch.airquality.repository.PollutionDensityRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AirQualityService {

    private final AirQualityRepository airQualityRepository;
    private final PollutionDensityRepository pollutionDensityRepository;
    private final KafkaProducerService kafkaProducerService;

    private final Random random = new Random();

    // Sample locations for demo purposes
    private final String[] locations = {
            "Istanbul", "Ankara", "Izmir", "Bursa", "Antalya",
            "Adana", "Konya", "Gaziantep", "Mersin", "Diyarbakir"
    };

    // Coordinates for the sample locations (approximate)
    private final double[][] coordinates = {
            {41.0082, 28.9784}, // Istanbul
            {39.9334, 32.8597}, // Ankara
            {38.4237, 27.1428}, // Izmir
            {40.1885, 29.0610}, // Bursa
            {36.8969, 30.7133}, // Antalya
            {37.0000, 35.3213}, // Adana
            {37.8667, 32.4833}, // Konya
            {37.0662, 37.3833}, // Gaziantep
            {36.8000, 34.6333}, // Mersin
            {37.9144, 40.2306}  // Diyarbakir
    };

    public List<AirQualityData> getAirQualityByLocation(String location) {
        return airQualityRepository.findByLocation(location);
    }

    public List<PollutionDensity> getPollutionDensityByRegion() {
        return pollutionDensityRepository.getPollutionDensityByRegion();
    }

    // Generate and save sample data every minute for demo purposes
    @Scheduled(fixedRate = 60000)
    public void generateSampleData() {
        log.info("Generating sample air quality data");

        for (int i = 0; i < locations.length; i++) {
            AirQualityData data = generateRandomAirQualityData(locations[i], coordinates[i][0], coordinates[i][1]);

            // Save to repository
            airQualityRepository.save(data);

            // Send to Kafka for anomaly detection
            kafkaProducerService.sendAirQualityData(data);
        }
    }

    private AirQualityData generateRandomAirQualityData(String location, double latitude, double longitude) {
        // Generate random but realistic air quality data
        double pm25 = 5 + random.nextDouble() * 45; // 5-50 µg/m³
        double pm10 = 10 + random.nextDouble() * 90; // 10-100 µg/m³
        double o3 = 0.02 + random.nextDouble() * 0.08; // 0.02-0.1 ppm
        double no2 = 10 + random.nextDouble() * 90; // 10-100 ppb
        double so2 = 5 + random.nextDouble() * 70; // 5-75 ppb
        double co = 0.5 + random.nextDouble() * 9.5; // 0.5-10 ppm

        // Calculate AQI (simplified)
        double aqi = calculateAQI(pm25, pm10, o3, no2, so2, co);

        // Occasionally generate anomalous data (10% chance)
        if (random.nextDouble() < 0.1) {
            // Choose a random pollutant to spike
            int pollutantToSpike = random.nextInt(6);
            switch (pollutantToSpike) {
                case 0:
                    pm25 *= 2.5; // Spike PM2.5
                    break;
                case 1:
                    pm10 *= 2.5; // Spike PM10
                    break;
                case 2:
                    o3 *= 2.5; // Spike O3
                    break;
                case 3:
                    no2 *= 2.5; // Spike NO2
                    break;
                case 4:
                    so2 *= 2.5; // Spike SO2
                    break;
                case 5:
                    co *= 2.5; // Spike CO
                    break;
            }

            // Recalculate AQI with the spiked value
            aqi = calculateAQI(pm25, pm10, o3, no2, so2, co);
        }

        return AirQualityData.builder()
                .id(UUID.randomUUID().toString())
                .location(location)
                .latitude(latitude)
                .longitude(longitude)
                .timestamp(Instant.now())
                .pm25(pm25)
                .pm10(pm10)
                .o3(o3)
                .no2(no2)
                .so2(so2)
                .co(co)
                .aqi(aqi)
                .source("sensor-simulation")
                .build();
    }

    // Simplified AQI calculation
    private double calculateAQI(double pm25, double pm10, double o3, double no2, double so2, double co) {
        // This is a very simplified calculation
        // In a real application, this would follow EPA or similar guidelines

        // Normalize each pollutant to a 0-500 scale and take the max
        double pm25Index = (pm25 / 50.0) * 100;
        double pm10Index = (pm10 / 150.0) * 100;
        double o3Index = (o3 / 0.1) * 100;
        double no2Index = (no2 / 100.0) * 100;
        double so2Index = (so2 / 75.0) * 100;
        double coIndex = (co / 10.0) * 100;

        return Math.max(Math.max(Math.max(pm25Index, pm10Index), Math.max(o3Index, no2Index)), Math.max(so2Index, coIndex));
    }
}
