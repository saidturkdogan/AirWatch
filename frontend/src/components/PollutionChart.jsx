import React, { useState, useMemo } from 'react';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from 'recharts';
import '../assets/css/PollutionChart.css';

function PollutionChart({ pollutionData, selectedRegion, loading }) {
  const [pollutantType, setPollutantType] = useState('PM2.5');
  
  const chartData = useMemo(() => {
    if (!pollutionData || pollutionData.length === 0) return [];
    
    let filteredData = pollutionData;
    
    // If a region is selected, filter data for that region
    if (selectedRegion) {
      filteredData = pollutionData.filter(
        item => 
          item.location.lat === selectedRegion.lat && 
          item.location.lng === selectedRegion.lng
      );
    } else {
      // If no region is selected, use data from all regions but calculate averages per day
      const dailyAverages = new Map();
      
      pollutionData.forEach(item => {
        if (!dailyAverages.has(item.date)) {
          dailyAverages.set(item.date, {
            date: item.date,
            totalValues: {},
            counts: {}
          });
        }
        
        const dayData = dailyAverages.get(item.date);
        
        Object.entries(item.measurements).forEach(([type, value]) => {
          if (!dayData.totalValues[type]) {
            dayData.totalValues[type] = 0;
            dayData.counts[type] = 0;
          }
          
          dayData.totalValues[type] += value;
          dayData.counts[type]++;
        });
      });
      
      // Convert to array and calculate averages
      return Array.from(dailyAverages.values())
        .map(item => {
          const result = { date: item.date };
          
          Object.entries(item.totalValues).forEach(([type, total]) => {
            result[type] = total / item.counts[type];
          });
          
          return result;
        })
        .sort((a, b) => new Date(a.date) - new Date(b.date));
    }
    
    // Format data for the chart
    return filteredData
      .map(item => ({
        date: item.date,
        ...item.measurements
      }))
      .sort((a, b) => new Date(a.date) - new Date(b.date));
  }, [pollutionData, selectedRegion]);
  
  const pollutantOptions = ['PM2.5', 'PM10', 'NO2', 'SO2', 'O3', 'CO'];
  
  // Function to get color for each pollutant
  const getPollutantColor = (type) => {
    switch(type) {
      case 'PM2.5': return '#8884d8';
      case 'PM10': return '#82ca9d';
      case 'NO2': return '#ffc658';
      case 'SO2': return '#ff8042';
      case 'O3': return '#0088fe';
      case 'CO': return '#ff5151';
      default: return '#8884d8';
    }
  };
  
  return (
    <div className="chart-container">
      <div className="chart-header">
        <h3>
          {selectedRegion 
            ? `Air Pollution Trends: ${selectedRegion.name}` 
            : 'Global Air Pollution Trends'}
        </h3>
        
        <div className="pollutant-selector">
          <label htmlFor="pollutant-select">Pollutant: </label>
          <select 
            id="pollutant-select"
            value={pollutantType}
            onChange={(e) => setPollutantType(e.target.value)}
          >
            {pollutantOptions.map(option => (
              <option key={option} value={option}>{option}</option>
            ))}
          </select>
        </div>
      </div>
      
      {loading ? (
        <div className="chart-loading">
          <div className="loading-spinner"></div>
          <p>Loading chart data...</p>
        </div>
      ) : chartData.length > 0 ? (
        <div className="chart-wrapper">
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={chartData} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis 
                dataKey="date" 
                tick={{ fontSize: 12 }}
                tickFormatter={(value) => {
                  const date = new Date(value);
                  return `${date.getMonth() + 1}/${date.getDate()}`;
                }}
              />
              <YAxis />
              <Tooltip 
                formatter={(value, name) => [value.toFixed(2), name]}
                labelFormatter={(label) => {
                  const date = new Date(label);
                  return date.toLocaleDateString();
                }}
              />
              <Legend />
              <Line 
                type="monotone" 
                dataKey={pollutantType}
                stroke={getPollutantColor(pollutantType)}
                activeDot={{ r: 8 }}
                strokeWidth={2}
                dot={{ strokeWidth: 2 }}
              />
            </LineChart>
          </ResponsiveContainer>
        </div>
      ) : (
        <div className="no-data-message">
          <p>No pollution data available for the selected parameters.</p>
        </div>
      )}
    </div>
  );
}

export default PollutionChart; 