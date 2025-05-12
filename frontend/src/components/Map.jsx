import React, { useMemo } from 'react';
import { MapContainer, TileLayer, Circle, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import '../assets/css/Map.css';

// Fix for marker icons in leaflet with webpack
import L from 'leaflet';
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon-2x.png',
  iconUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-shadow.png',
});

function PollutionMap({ pollutionData, onRegionSelect, loading }) {
  // Calculate average pollution level for each city to determine circle color and size
  const aggregatedData = useMemo(() => {
    if (!pollutionData || pollutionData.length === 0) return [];
    
    // Group by location
    const locationMap = new Map();
    
    pollutionData.forEach(item => {
      const key = `${item.location.lat},${item.location.lng}`;
      
      if (!locationMap.has(key)) {
        locationMap.set(key, {
          location: item.location,
          totalMeasurements: 0,
          sumValues: 0,
          dataPoints: 0
        });
      }
      
      const locationData = locationMap.get(key);
      
      // Sum up all measurement values
      Object.values(item.measurements).forEach(value => {
        locationData.sumValues += value;
        locationData.dataPoints++;
      });
      
      locationData.totalMeasurements++;
    });
    
    // Convert map to array and calculate averages
    return Array.from(locationMap.values()).map(item => ({
      location: item.location,
      avgPollution: item.sumValues / item.dataPoints,
      count: item.totalMeasurements
    }));
  }, [pollutionData]);
  
  // Function to determine color based on pollution level
  const getPollutionColor = (level) => {
    if (level > 300) return '#7e0023'; // Hazardous
    if (level > 200) return '#99004c'; // Very Unhealthy
    if (level > 150) return '#ff0000'; // Unhealthy
    if (level > 100) return '#ff7e00'; // Unhealthy for Sensitive Groups
    if (level > 50) return '#ffff00';  // Moderate
    return '#00e400';                 // Good
  };
  
  // Function to determine radius based on pollution level
  const getCircleRadius = (level) => {
    return Math.max(20000, Math.min(100000, level * 300));
  };
  
  return (
    <div className="map-wrapper">
      {loading ? (
        <div className="loading-overlay">
          <div className="loading-spinner"></div>
          <p>Loading pollution data...</p>
        </div>
      ) : null}
      
      <MapContainer 
        center={[39.0, 35.0]} 
        zoom={6} 
        style={{ height: '100%', width: '100%' }}
        zoomControl={false}
      >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        />
        
        {aggregatedData.map((item, index) => (
          <Circle
            key={`${item.location.name}-${index}`}
            center={[item.location.lat, item.location.lng]}
            radius={getCircleRadius(item.avgPollution)}
            pathOptions={{
              fillColor: getPollutionColor(item.avgPollution),
              color: getPollutionColor(item.avgPollution),
              fillOpacity: 0.5,
              weight: 1
            }}
            eventHandlers={{
              click: () => onRegionSelect(item.location)
            }}
          >
            <Popup>
              <div className="popup-content">
                <h3>{item.location.name}</h3>
                <p>Average Pollution Level: {item.avgPollution.toFixed(1)}</p>
                <p className="popup-hint">Click for detailed analysis</p>
              </div>
            </Popup>
          </Circle>
        ))}
      </MapContainer>
      
      <div className="map-legend">
        <h4>Air Quality Index</h4>
        <div className="legend-item">
          <span className="legend-color" style={{ backgroundColor: '#00e400' }}></span>
          <span>Good (0-50)</span>
        </div>
        <div className="legend-item">
          <span className="legend-color" style={{ backgroundColor: '#ffff00' }}></span>
          <span>Moderate (51-100)</span>
        </div>
        <div className="legend-item">
          <span className="legend-color" style={{ backgroundColor: '#ff7e00' }}></span>
          <span>Unhealthy for Sensitive Groups (101-150)</span>
        </div>
        <div className="legend-item">
          <span className="legend-color" style={{ backgroundColor: '#ff0000' }}></span>
          <span>Unhealthy (151-200)</span>
        </div>
        <div className="legend-item">
          <span className="legend-color" style={{ backgroundColor: '#99004c' }}></span>
          <span>Very Unhealthy (201-300)</span>
        </div>
        <div className="legend-item">
          <span className="legend-color" style={{ backgroundColor: '#7e0023' }}></span>
          <span>Hazardous (&gt;300)</span>
        </div>
      </div>
    </div>
  );
}

export default PollutionMap; 