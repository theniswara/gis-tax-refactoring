// src/environments/environment.ts
// Development environment configuration

export const environment = {
  production: false,
  //apiUrl: 'http://localhost:8080/api',
  apiUrl: '/api',
  
  // Security settings
  tokenKey: 'auth_token',
  refreshTokenKey: 'refresh_token',
  
  // Session timeout (in milliseconds) - 24 hours
  sessionTimeout: 86400000,
  
  // Auto-refresh token before expiration (5 minutes before)
  tokenRefreshBuffer: 300000
};
