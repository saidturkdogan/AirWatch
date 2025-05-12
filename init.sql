-- Create extension for TimescaleDB
CREATE EXTENSION IF NOT EXISTS timescaledb CASCADE;

-- Create tables if they don't exist
CREATE TABLE IF NOT EXISTS air_quality_data (
    id VARCHAR(255) PRIMARY KEY,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    location VARCHAR(255),
    timestamp TIMESTAMPTZ,
    pm25 DOUBLE PRECISION,
    pm10 DOUBLE PRECISION,
    o3 DOUBLE PRECISION,
    no2 DOUBLE PRECISION,
    so2 DOUBLE PRECISION,
    co DOUBLE PRECISION,
    aqi DOUBLE PRECISION,
    source VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS anomalies (
    id VARCHAR(255) PRIMARY KEY,
    location VARCHAR(255),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    detected_at TIMESTAMPTZ,
    pollutant_type VARCHAR(255),
    value DOUBLE PRECISION,
    threshold DOUBLE PRECISION,
    severity VARCHAR(255),
    description TEXT
);

CREATE TABLE IF NOT EXISTS notifications (
    id VARCHAR(255) PRIMARY KEY,
    type VARCHAR(255),
    message TEXT,
    severity VARCHAR(255),
    timestamp TIMESTAMPTZ,
    read BOOLEAN,
    source VARCHAR(255)
);

-- Create hypertables
SELECT create_hypertable('air_quality_data', 'timestamp', if_not_exists => TRUE);
SELECT create_hypertable('anomalies', 'detected_at', if_not_exists => TRUE);
SELECT create_hypertable('notifications', 'timestamp', if_not_exists => TRUE);

-- Insert initial data for cities
INSERT INTO air_quality_data (id, latitude, longitude, location, timestamp, pm25, pm10, o3, no2, so2, co, aqi, source)
VALUES
    ('1', 41.0082, 28.9784, 'Istanbul', NOW(), 25.5, 45.2, 0.045, 35.8, 15.2, 2.5, 75.3, 'initial-data'),
    ('2', 39.9334, 32.8597, 'Ankara', NOW(), 18.2, 32.7, 0.038, 28.4, 12.1, 1.8, 62.1, 'initial-data'),
    ('3', 38.4237, 27.1428, 'Izmir', NOW(), 15.8, 28.3, 0.042, 25.6, 10.5, 1.5, 58.4, 'initial-data'),
    ('4', 40.1885, 29.0610, 'Bursa', NOW(), 22.3, 38.9, 0.040, 30.2, 14.8, 2.2, 68.5, 'initial-data'),
    ('5', 36.8969, 30.7133, 'Antalya', NOW(), 12.4, 22.8, 0.035, 18.7, 8.3, 1.2, 45.2, 'initial-data'),
    ('6', 37.0000, 35.3213, 'Adana', NOW(), 28.7, 52.4, 0.048, 42.3, 18.9, 3.1, 82.6, 'initial-data'),
    ('7', 37.8667, 32.4833, 'Konya', NOW(), 20.1, 36.5, 0.039, 27.8, 13.2, 2.0, 65.3, 'initial-data'),
    ('8', 37.0662, 37.3833, 'Gaziantep', NOW(), 30.2, 55.8, 0.052, 45.6, 20.3, 3.5, 88.7, 'initial-data'),
    ('9', 36.8000, 34.6333, 'Mersin', NOW(), 16.9, 30.2, 0.037, 24.5, 11.8, 1.7, 56.8, 'initial-data'),
    ('10', 37.9144, 40.2306, 'Diyarbakir', NOW(), 26.8, 48.3, 0.047, 38.9, 17.5, 2.8, 78.4, 'initial-data');
