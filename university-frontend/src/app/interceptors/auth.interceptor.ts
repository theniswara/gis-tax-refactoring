// src/app/interceptors/auth.interceptor.ts
import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { environment } from '../../environments/environment';

/**
 * Authentication Interceptor (Functional)
 * 
 * Automatically adds JWT token to HTTP requests
 * Supports both Student and Admin tokens
 * Handles authentication errors
 * SSR-compatible using isPlatformBrowser check
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const platformId = inject(PLATFORM_ID);

  let adminToken: string | null = null;
  let studentToken: string | null = null;
  let token: string | null = null;

  // ✅ Get tokens from localStorage (only in browser)
  if (isPlatformBrowser(platformId)) {
    // Check admin token first, then student token
    adminToken = localStorage.getItem('adminToken');
    // ✅ FIXED: Use environment.tokenKey instead of hardcoded 'token'
    studentToken = localStorage.getItem(environment.tokenKey);
    token = adminToken || studentToken;
  }

  // ✅ Clone request and add token if available
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log('✅ Authorization header added to request:', req.url);
  } else {
    console.warn('⚠️ No token found for request:', req.url);
  }

  // ✅ Handle the request and catch errors
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      // Only handle localStorage in browser environment
      if (isPlatformBrowser(platformId)) {
        // ✅ Handle 401 Unauthorized - Token invalid or expired
        if (error.status === 401) {
          console.error('🔴 401 Unauthorized - Token invalid or expired');
          if (adminToken) {
            // Admin token expired
            localStorage.removeItem('adminToken');
            localStorage.removeItem('adminRefreshToken');
            localStorage.removeItem('adminUser');
            router.navigate(['/admin/login']);
          } else if (studentToken) {
            // Student token expired - clean up using environment keys
            localStorage.removeItem(environment.tokenKey);
            localStorage.removeItem(environment.refreshTokenKey);
            localStorage.removeItem('currentStudent');
            router.navigate(['/portal']);
          }
        }

        // ✅ Handle 403 Forbidden - Insufficient permissions
        if (error.status === 403) {
          console.error('🔴 403 Forbidden - Access denied');
          if (adminToken) {
            // Admin doesn't have permission
            console.error('Admin access forbidden');
            router.navigate(['/admin/dashboard']);
          } else if (studentToken) {
            // Student doesn't have permission
            console.error('Student access forbidden');
            // Check if token exists at all
            console.log('Student token exists:', !!studentToken);
            console.log('Request URL:', error.url);
          }
        }
      }

      return throwError(() => error);
    })
  );
};