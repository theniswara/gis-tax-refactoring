// src/environments/environment.prod.ts
// Production environment configuration

export const environment = {
  production: true,
  apiUrl: 'https://api.yourdomain.com/api', // ✅ Change this to your production API URL
  
  // Security settings
  tokenKey: 'auth_token',
  refreshTokenKey: 'refresh_token',
  
  // Session timeout (in milliseconds) - 24 hours
  sessionTimeout: 86400000,
  
  // Auto-refresh token before expiration (5 minutes before)
  tokenRefreshBuffer: 300000
};
