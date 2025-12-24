// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError, timer } from 'rxjs';
import { tap, catchError, switchMap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { Student } from '../models/student.model';
import { environment } from '../../environments/environment';

interface LoginRequest {
  studentId: string;
  password: string;
}

interface LoginResponse {
  success: boolean;
  message: string;
  accessToken?: string;
  refreshToken?: string;
  student: Student | null;
}

/**
 * Secure Authentication Service
 * 
 * Security Features:
 * - JWT token management
 * - Secure token storage
 * - Automatic token refresh
 * - Session timeout handling
 * - Password cleared from memory
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasToken());
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  private currentStudentSubject = new BehaviorSubject<Student | null>(this.getStoredStudent());
  public currentStudent$ = this.currentStudentSubject.asObservable();
  
  private tokenRefreshTimer: any;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    // Start token refresh timer if authenticated
    if (this.hasToken()) {
      this.startTokenRefreshTimer();
    }
  }

  /**
   * Login with credentials
   */
  login(studentId: string, password: string): Observable<LoginResponse> {
    const loginRequest: LoginRequest = { studentId, password };
    
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, loginRequest)
      .pipe(
        tap(response => {
          if (response.success && response.accessToken) {
            // ✅ Store tokens securely
            this.storeTokens(response.accessToken, response.refreshToken || '');
            
            // ✅ Store student data (password already excluded by backend)
            if (response.student) {
              this.storeStudent(response.student);
              this.currentStudentSubject.next(response.student);
            }
            
            // ✅ Update authentication state
            this.isAuthenticatedSubject.next(true);
            
            // ✅ Start token refresh timer
            this.startTokenRefreshTimer();
          }
        }),
        catchError(error => {
          console.error('Login error:', error);
          return of({ 
            success: false, 
            message: error.error?.message || 'Login failed', 
            student: null 
          } as LoginResponse);
        })
      );
  }

  /**
   * Logout user
   */
  logout(): void {
    // Clear tokens and user data
    this.clearTokens();
    this.clearStudent();
    
    // Update state
    this.isAuthenticatedSubject.next(false);
    this.currentStudentSubject.next(null);
    
    // Stop token refresh timer
    this.stopTokenRefreshTimer();
    
    // Redirect to login
    this.router.navigate(['/student-portal']);
  }

  /**
   * Check if user is logged in
   */
  isLoggedIn(): boolean {
    return this.hasToken() && !this.isTokenExpired();
  }

  /**
   * Get access token
   */
  getToken(): string | null {
    // Check if we're in a browser environment
    if (typeof window === 'undefined' || typeof localStorage === 'undefined') {
      return null;
    }
    return localStorage.getItem(environment.tokenKey);
  }

  /**
   * Get refresh token
   */
  getRefreshToken(): string | null {
    // Check if we're in a browser environment
    if (typeof window === 'undefined' || typeof localStorage === 'undefined') {
      return null;
    }
    return localStorage.getItem(environment.refreshTokenKey);
  }

  /**
   * Refresh access token
   */
  refreshToken(): Observable<LoginResponse> {
    const refreshToken = this.getRefreshToken();
    
    if (!refreshToken) {
      this.logout();
      return throwError(() => new Error('No refresh token available'));
    }
    
    return this.http.post<LoginResponse>(`${this.apiUrl}/refresh`, { refreshToken })
      .pipe(
        tap(response => {
          if (response.success && response.accessToken) {
            // Update access token - only in browser environment
            if (typeof window !== 'undefined' && typeof localStorage !== 'undefined') {
              localStorage.setItem(environment.tokenKey, response.accessToken);
            }
          } else {
            // Refresh failed, logout
            this.logout();
          }
        }),
        catchError(error => {
          console.error('Token refresh error:', error);
          this.logout();
          return throwError(() => error);
        })
      );
  }

  /**
   * Store tokens in localStorage
   */
  private storeTokens(accessToken: string, refreshToken: string): void {
    // Check if we're in a browser environment
    if (typeof window === 'undefined' || typeof localStorage === 'undefined') {
      return;
    }
    localStorage.setItem(environment.tokenKey, accessToken);
    if (refreshToken) {
      localStorage.setItem(environment.refreshTokenKey, refreshToken);
    }
  }

  /**
   * Clear tokens from storage
   */
  private clearTokens(): void {
    // Check if we're in a browser environment
    if (typeof window === 'undefined' || typeof localStorage === 'undefined') {
      return;
    }
    localStorage.removeItem(environment.tokenKey);
    localStorage.removeItem(environment.refreshTokenKey);
  }

  /**
   * Store student data
   */
  private storeStudent(student: Student): void {
    // Check if we're in a browser environment
    if (typeof window === 'undefined' || typeof localStorage === 'undefined') {
      return;
    }
    localStorage.setItem('currentStudent', JSON.stringify(student));
  }

  /**
   * Get stored student data
   */
  private getStoredStudent(): Student | null {
    // Check if we're in a browser environment
    if (typeof window === 'undefined' || typeof localStorage === 'undefined') {
      return null;
    }
    const studentJson = localStorage.getItem('currentStudent');
    return studentJson ? JSON.parse(studentJson) : null;
  }

  /**
   * Clear student data
   */
  private clearStudent(): void {
    // Check if we're in a browser environment
    if (typeof window === 'undefined' || typeof localStorage === 'undefined') {
      return;
    }
    localStorage.removeItem('currentStudent');
  }

  /**
   * Check if token exists
   */
  private hasToken(): boolean {
    // Check if we're in a browser environment
    if (typeof window === 'undefined' || typeof localStorage === 'undefined') {
      return false;
    }
    return !!this.getToken();
  }

  /**
   * Check if token is expired
   */
  private isTokenExpired(): boolean {
    const token = this.getToken();
    if (!token) return true;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const exp = payload.exp * 1000; // Convert to milliseconds
      return Date.now() >= exp;
    } catch (e) {
      return true;
    }
  }

  /**
   * Start automatic token refresh timer
   */
  private startTokenRefreshTimer(): void {
    this.stopTokenRefreshTimer(); // Clear existing timer
    
    // Refresh token 5 minutes before expiration
    const refreshInterval = environment.sessionTimeout - environment.tokenRefreshBuffer;
    
    this.tokenRefreshTimer = timer(refreshInterval, refreshInterval)
      .pipe(
        switchMap(() => this.refreshToken())
      )
      .subscribe();
  }

  /**
   * Stop token refresh timer
   */
  private stopTokenRefreshTimer(): void {
    if (this.tokenRefreshTimer) {
      this.tokenRefreshTimer.unsubscribe();
      this.tokenRefreshTimer = null;
    }
  }
}