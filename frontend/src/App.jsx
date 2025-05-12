import React, { useState, useEffect } from 'react'
import './App.css'
import './assets/css/Header.css'
import Header from './components/Header'
import PollutionMap from './components/Map'
import PollutionChart from './components/PollutionChart'
import WarningPanel from './components/WarningPanel'
import RegionDetail from './components/RegionDetail'
import axios from 'axios'

function App() {
  const [pollutionData, setPollutionData] = useState([]);
  const [anomalies, setAnomalies] = useState([]);
  const [selectedRegion, setSelectedRegion] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // List of cities to fetch data for
  const cities = [
    { name: 'Istanbul', lat: 41.0082, lng: 28.9784 },
    { name: 'Ankara', lat: 39.9334, lng: 32.8597 },
    { name: 'Izmir', lat: 38.4237, lng: 27.1428 },
    { name: 'Antalya', lat: 36.8969, lng: 30.7133 },
    { name: 'Bursa', lat: 40.1885, lng: 29.0610 },
    { name: 'Adana', lat: 37.0000, lng: 35.3213 },
    { name: 'Konya', lat: 37.8715, lng: 32.4846 },
    { name: 'Gaziantep', lat: 37.0662, lng: 37.3833 },
    { name: 'Samsun', lat: 41.2867, lng: 36.3300 },
    { name: 'Kayseri', lat: 38.7205, lng: 35.4784 },
    { name: 'Mersin', lat: 36.8000, lng: 34.6333 },
    { name: 'Eskisehir', lat: 39.7767, lng: 30.5206 },
    { name: 'Diyarbakir', lat: 37.9155, lng: 40.2306 },
    { name: 'London', lat: 51.5074, lng: -0.1278 },
    { name: 'New York', lat: 40.7128, lng: -74.0060 },
    { name: 'Tokyo', lat: 35.6762, lng: 139.6503 }
  ];

  const pollutantTypes = ['PM2.5', 'PM10', 'NO2', 'SO2', 'O3', 'CO'];
  
  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        setError(null);
        
        // Create an array to store all pollution data
        let allPollutionData = [];
        const detectedAnomalies = [];
        
        // Fetch data for each city
        for (const city of cities) {
          try {
            // Önce İngilizce formatta şehir adını deneyin
            let response = await axios.get(`http://localhost:8484/api/air-quality`, {
              params: { location: city.name }
            });
            
            console.log(`API response for ${city.name}:`, response.data);
            
            // Veri boş dizi ise veya veri yoksa, Türkçe formatta tekrar deneyin
            if (!response.data || (Array.isArray(response.data) && response.data.length === 0)) {
              // Şehir adını Türkçe karakterlerle eşleştirelim
              const turkishNames = {
                'Istanbul': 'İstanbul',
                'Izmir': 'İzmir',
                'Diyarbakir': 'Diyarbakır',
                'Eskisehir': 'Eskişehir'
              };
              
              if (turkishNames[city.name]) {
                console.log(`Trying with Turkish name: ${turkishNames[city.name]}`);
                response = await axios.get(`http://localhost:8484/api/air-quality`, {
                  params: { location: turkishNames[city.name] }
                });
                console.log(`API response for ${turkishNames[city.name]}:`, response.data);
              }
            }
            
            // Extract measurements from the response
            let measurements = {};
            
            // Handle different possible response formats
            if (response.data) {
              if (response.data.measurements) {
                // If API returns data in the expected format
                measurements = response.data.measurements;
              } else if (response.data.data && response.data.data.measurements) {
                // Alternative format: nested under 'data'
                measurements = response.data.data.measurements;
              } else if (Array.isArray(response.data) && response.data.length > 0) {
                // If response is an array (like in our actual API)
                // First try to find the specific city by exact match on "location" field
                const cityData = response.data.find(item => {
                  if (!item.location) return false;
                  
                  // Normalizasyon: tüm boşlukları kaldır, küçük harfe çevir, Türkçe karakterleri İngilizce olanlarla değiştir
                  const normalizeText = (text) => {
                    return text.toLowerCase()
                      .replace(/\s+/g, '')
                      .replace(/ı/g, 'i')
                      .replace(/ğ/g, 'g')
                      .replace(/ü/g, 'u')
                      .replace(/ş/g, 's')
                      .replace(/ö/g, 'o')
                      .replace(/ç/g, 'c');
                  };
                  
                  const normalizedItemLocation = normalizeText(item.location);
                  const normalizedCityName = normalizeText(city.name);
                  
                  return normalizedItemLocation === normalizedCityName || 
                         normalizedItemLocation.includes(normalizedCityName) || 
                         normalizedCityName.includes(normalizedItemLocation);
                });
                
                // If city match not found, use the first result (could be filtered by backend already)
                const dataToUse = cityData || response.data[0];
                
                console.log(`Processing data for ${city.name}:`, dataToUse);
                
                // Map specific fields to our measurements format based on API response
                const fieldMapping = {
                  'pm25': 'PM2.5',
                  'pm10': 'PM10',
                  'no2': 'NO2',
                  'so2': 'SO2',
                  'o3': 'O3',
                  'co': 'CO'
                };
                
                Object.entries(fieldMapping).forEach(([apiKey, appKey]) => {
                  if (dataToUse[apiKey] !== undefined) {
                    measurements[appKey] = parseFloat(dataToUse[apiKey]);
                  }
                });
                
                console.log(`Extracted measurements for ${city.name}:`, measurements);
                
                // If no measurements were found, generate fallback data
                if (Object.keys(measurements).length === 0) {
                  console.warn(`No valid measurements found for ${city.name}, using fallback data`);
                  measurements = generateFallbackMeasurements();
                }
              } else {
                // If the response itself is the measurements object
                // Filter out non-numeric values and known metadata fields
                const knownMetadataFields = ['location', 'city', 'date', 'timestamp', 'status'];
                measurements = Object.fromEntries(
                  Object.entries(response.data)
                    .filter(([key, value]) => 
                      !knownMetadataFields.includes(key) && 
                      typeof value === 'number'
                    )
                );
                
                // If we couldn't find any measurements, generate fallback data
                if (Object.keys(measurements).length === 0) {
                  measurements = generateFallbackMeasurements();
                }
              }
            }
            
            // Format the data to match our application's data structure
            const cityData = {
              location: { 
                name: city.name,
                lat: city.lat, 
                lng: city.lng 
              },
              date: new Date().toISOString().split('T')[0], // Today's date
              measurements: measurements
            };
            
            allPollutionData.push(cityData);
            
            // Check for anomalies in the data
            Object.entries(measurements).forEach(([type, value]) => {
              // Define threshold for anomalies (adjust as needed)
              const threshold = 200;
              if (value > threshold) {
                detectedAnomalies.push({
                  id: `${city.name}-${type}-${new Date().toISOString()}`,
                  location: { ...city },
                  pollutantType: type,
                  value: value,
                  date: new Date().toISOString().split('T')[0],
                  threshold: threshold
                });
              }
            });
          } catch (cityError) {
            console.error(`Error fetching data for ${city.name}:`, cityError);
            
            // Generate fallback data if API call fails for this city
            const fallbackData = generateFallbackDataForCity(city);
            allPollutionData.push(fallbackData);
          }
        }
        
        setPollutionData(allPollutionData);
        setAnomalies(detectedAnomalies);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching data:', error);
        setError('Failed to fetch pollution data. Using fallback data instead.');
        
        // Use fallback data generation if API calls completely fail
        const fallbackData = generateFallbackData();
        setPollutionData(fallbackData.pollutionData);
        setAnomalies(fallbackData.anomalies);
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleRegionSelect = (region) => {
    setSelectedRegion(region);
  };

  return (
    <div className="app-container">
      <Header />
      
      {error && (
        <div className="error-notification">
          {error}
        </div>
      )}
      
      <main className="main-content">
        <div className="dashboard">
          <div className="map-container">
            <PollutionMap 
              pollutionData={pollutionData} 
              onRegionSelect={handleRegionSelect}
              loading={loading}
            />
          </div>
          
          <div className="data-panels">
            <WarningPanel anomalies={anomalies} loading={loading} />
            <PollutionChart 
              pollutionData={pollutionData} 
              selectedRegion={selectedRegion}
              loading={loading}
            />
          </div>
        </div>

        {selectedRegion && (
          <RegionDetail 
            region={selectedRegion} 
            pollutionData={pollutionData.filter(item => 
              item.location.lat === selectedRegion.lat && 
              item.location.lng === selectedRegion.lng
            )}
          />
        )}
      </main>
    </div>
  );
}

// Generate fallback measurements
function generateFallbackMeasurements() {
  const measurements = {};
  const pollutantTypes = ['PM2.5', 'PM10', 'NO2', 'SO2', 'O3', 'CO'];
  
  pollutantTypes.forEach(type => {
    measurements[type] = Math.floor(Math.random() * 200);
  });
  
  return measurements;
}

// Generate fallback data for a specific city
function generateFallbackDataForCity(city) {
  return {
    location: { 
      name: city.name,
      lat: city.lat, 
      lng: city.lng 
    },
    date: new Date().toISOString().split('T')[0],
    measurements: generateFallbackMeasurements()
  };
}

// Generate fallback data for all cities if API completely fails
function generateFallbackData() {
  const cities = [
    { name: 'Istanbul', lat: 41.0082, lng: 28.9784 },
    { name: 'Ankara', lat: 39.9334, lng: 32.8597 },
    { name: 'Izmir', lat: 38.4237, lng: 27.1428 },
    { name: 'Antalya', lat: 36.8969, lng: 30.7133 },
    { name: 'Bursa', lat: 40.1885, lng: 29.0610 },
    { name: 'Adana', lat: 37.0000, lng: 35.3213 },
    { name: 'Konya', lat: 37.8715, lng: 32.4846 },
    { name: 'Gaziantep', lat: 37.0662, lng: 37.3833 },
    { name: 'Samsun', lat: 41.2867, lng: 36.3300 },
    { name: 'Kayseri', lat: 38.7205, lng: 35.4784 },
    { name: 'Mersin', lat: 36.8000, lng: 34.6333 },
    { name: 'Eskisehir', lat: 39.7767, lng: 30.5206 },
    { name: 'Diyarbakir', lat: 37.9155, lng: 40.2306 },
    { name: 'London', lat: 51.5074, lng: -0.1278 },
    { name: 'New York', lat: 40.7128, lng: -74.0060 },
    { name: 'Tokyo', lat: 35.6762, lng: 139.6503 }
  ];

  const pollutantTypes = ['PM2.5', 'PM10', 'NO2', 'SO2', 'O3', 'CO'];
  
  const pollutionData = [];
  const anomalies = [];

  cities.forEach(city => {
    const dailyData = {
      location: { 
        name: city.name,
        lat: city.lat, 
        lng: city.lng 
      },
      date: new Date().toISOString().split('T')[0],
      measurements: {}
    };

    pollutantTypes.forEach(type => {
      // Generate random pollution level
      let value = Math.floor(Math.random() * 200);
      
      // Occasionally generate anomalies (high values)
      const isAnomaly = Math.random() < 0.05; // 5% chance of anomaly
      if (isAnomaly) {
        value = Math.floor(Math.random() * 300) + 200; // 200-500 range for anomalies
        anomalies.push({
          id: `${city.name}-${type}-${new Date().toISOString()}`,
          location: { ...city },
          pollutantType: type,
          value: value,
          date: new Date().toISOString().split('T')[0],
          threshold: 200
        });
      }
      
      dailyData.measurements[type] = value;
    });
    
    pollutionData.push(dailyData);
  });

  return { pollutionData, anomalies };
}

export default App
