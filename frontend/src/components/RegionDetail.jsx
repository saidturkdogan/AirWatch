import React, { useMemo } from 'react';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from 'recharts';
import '../assets/css/RegionDetail.css';

function RegionDetail({ region, pollutionData }) {
  // Calculate average pollution values for each pollutant type
  const pollutantAverages = useMemo(() => {
    if (!pollutionData || pollutionData.length === 0) return [];
    
    const pollutantTotals = {};
    const pollutantCounts = {};
    
    // Calculate totals and counts for each pollutant
    pollutionData.forEach(item => {
      Object.entries(item.measurements).forEach(([type, value]) => {
        if (!pollutantTotals[type]) {
          pollutantTotals[type] = 0;
          pollutantCounts[type] = 0;
        }
        
        pollutantTotals[type] += value;
        pollutantCounts[type]++;
      });
    });
    
    // Calculate averages and convert to array for chart
    return Object.entries(pollutantTotals).map(([type, total]) => ({
      name: type,
      value: total / pollutantCounts[type]
    }));
  }, [pollutionData]);
  
  // Get latest data for the region
  const latestData = useMemo(() => {
    if (!pollutionData || pollutionData.length === 0) return null;
    
    // Sort by date (newest first) and take the first item
    return [...pollutionData].sort((a, b) => 
      new Date(b.date) - new Date(a.date)
    )[0];
  }, [pollutionData]);
  
  // Function to determine pollution level description
  const getPollutionLevel = (value) => {
    if (value > 300) return { text: 'Hazardous', color: '#7e0023' };
    if (value > 200) return { text: 'Very Unhealthy', color: '#99004c' };
    if (value > 150) return { text: 'Unhealthy', color: '#ff0000' };
    if (value > 100) return { text: 'Unhealthy for Sensitive Groups', color: '#ff7e00' };
    if (value > 50) return { text: 'Moderate', color: '#ffff00' };
    return { text: 'Good', color: '#00e400' };
  };
  
  // Calculate overall air quality index
  const averageAQI = useMemo(() => {
    if (pollutantAverages.length === 0) return 0;
    
    const sum = pollutantAverages.reduce((acc, item) => acc + item.value, 0);
    return sum / pollutantAverages.length;
  }, [pollutantAverages]);
  
  const aqi = getPollutionLevel(averageAQI);
  
  // Function to get color for each pollutant type bar
  const getPollutantColor = (name) => {
    switch(name) {
      case 'PM2.5': return '#8884d8';
      case 'PM10': return '#82ca9d';
      case 'NO2': return '#ffc658';
      case 'SO2': return '#ff8042';
      case 'O3': return '#0088fe';
      case 'CO': return '#ff5151';
      default: return '#8884d8';
    }
  };
  
  if (!region) return null;
  
  return (
    <div className="region-detail">
      <div className="region-header">
        <h2>{region.name} Air Quality Details</h2>
        <div className="region-coordinates">
          <span>Lat: {region.lat.toFixed(4)}</span>
          <span>Lng: {region.lng.toFixed(4)}</span>
        </div>
      </div>
      
      <div className="region-summary">
        <div className="aqi-indicator" style={{ backgroundColor: aqi.color }}>
          <div className="aqi-value">{Math.round(averageAQI)}</div>
          <div className="aqi-label">AQI</div>
        </div>
        
        <div className="aqi-description">
          <h4>Air Quality: <span style={{ color: aqi.color }}>{aqi.text}</span></h4>
          <p>
            The air quality in {region.name} is currently {aqi.text.toLowerCase()}.
            {averageAQI > 100 && 
              ' Sensitive groups may experience health effects. Consider reducing outdoor activities.'}
            {averageAQI > 150 && 
              ' General public may experience health effects. Limit outdoor exertion.'}
            {averageAQI > 200 && 
              ' Health alert: Risk of health effects is increased for everyone.'}
            {averageAQI > 300 && 
              ' Health warning of emergency conditions: Everyone is more likely to be affected.'}
          </p>
        </div>
      </div>
      
      <div className="region-chart-section">
        <h3>Pollutant Levels</h3>
        <div className="pollutant-chart">
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={pollutantAverages} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip formatter={(value) => [value.toFixed(2), 'Average Level']} />
              <Legend />
              <Bar 
                dataKey="value" 
                name="Pollution Level" 
                fill="#8884d8"
                radius={[5, 5, 0, 0]}
                barSize={40}
              />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
      
      {latestData && (
        <div className="latest-data-section">
          <h3>Latest Measurements ({new Date(latestData.date).toLocaleDateString()})</h3>
          <div className="latest-measurements">
            {Object.entries(latestData.measurements).map(([type, value]) => {
              const level = getPollutionLevel(value);
              return (
                <div key={type} className="measurement-item">
                  <div className="measurement-type">{type}</div>
                  <div className="measurement-value-container">
                    <div 
                      className="measurement-bar" 
                      style={{ 
                        width: `${Math.min(100, (value / 3))}%`, 
                        backgroundColor: getPollutantColor(type)
                      }}
                    ></div>
                    <div className="measurement-value">{value.toFixed(1)}</div>
                  </div>
                  <div 
                    className="measurement-level" 
                    style={{ color: level.color }}
                  >
                    {level.text}
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      )}
    </div>
  );
}

export default RegionDetail; 