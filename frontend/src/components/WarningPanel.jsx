import React from 'react';
import '../assets/css/WarningPanel.css';

function WarningPanel({ anomalies, loading }) {
  // Sort anomalies by date (newest first)
  const sortedAnomalies = [...(anomalies || [])].sort((a, b) => 
    new Date(b.date) - new Date(a.date)
  );
  
  // Format date to be more readable
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { 
      year: 'numeric', 
      month: 'short', 
      day: 'numeric' 
    });
  };
  
  return (
    <div className="warning-panel">
      <div className="warning-header">
        <h3>Pollution Alerts</h3>
        {sortedAnomalies.length > 0 && (
          <div className="alert-badge">{sortedAnomalies.length}</div>
        )}
      </div>
      
      {loading ? (
        <div className="warning-loading">
          <div className="loading-spinner"></div>
          <p>Loading alerts...</p>
        </div>
      ) : sortedAnomalies.length > 0 ? (
        <div className="warning-list">
          {sortedAnomalies.slice(0, 5).map((anomaly) => (
            <div key={anomaly.id} className="warning-item">
              <div className="warning-icon">⚠️</div>
              <div className="warning-content">
                <div className="warning-title">
                  <span className="location">{anomaly.location.name}</span>
                  <span className="date">{formatDate(anomaly.date)}</span>
                </div>
                <div className="warning-details">
                  <span className="pollutant">{anomaly.pollutantType}:</span>
                  <span className="value">{anomaly.value.toFixed(1)}</span>
                  <span className="unit">(threshold: {anomaly.threshold})</span>
                </div>
              </div>
            </div>
          ))}
          
          {sortedAnomalies.length > 5 && (
            <div className="warning-more">
              + {sortedAnomalies.length - 5} more alerts
            </div>
          )}
        </div>
      ) : (
        <div className="no-warnings">
          <div className="no-warnings-icon">✓</div>
          <p>No pollution alerts at this time.</p>
        </div>
      )}
    </div>
  );
}

export default WarningPanel; 