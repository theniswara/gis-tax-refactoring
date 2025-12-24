import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { 
  Admin, 
  AdminLoginRequest, 
  AdminLoginResponse, 
  CreateAdminRequest,
  DashboardStats 
} from '../models/admin.model';
import { Student, UpdateStudentRequest } from '../models/student.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = `${environment.apiUrl}/admin`;
  private currentAdminSubject = new BehaviorSubject<Admin | null>(null);
  public currentAdmin$ = this.currentAdminSubject.asObservable();
  private platformId = inject(PLATFORM_ID);

  constructor(private http: HttpClient) {
    // Load admin from localStorage on service initialization (only in browser)
    if (isPlatformBrowser(this.platformId)) {
      const adminData = localStorage.getItem('adminUser');
      if (adminData) {
        this.currentAdminSubject.next(JSON.parse(adminData));
      }
    }
  }

  /**
   * Admin login
   */
  login(credentials: AdminLoginRequest): Observable<AdminLoginResponse> {
    return this.http.post<AdminLoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        if (response.success && response.accessToken && isPlatformBrowser(this.platformId)) {
          // Store tokens
          localStorage.setItem('adminToken', response.accessToken);
          localStorage.setItem('adminRefreshToken', response.refreshToken);
          localStorage.setItem('adminUser', JSON.stringify(response.user));
          
          // Update current admin
          this.currentAdminSubject.next(response.user);
        }
      })
    );
  }

  /**
   * Admin logout
   */
  logout(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('adminToken');
      localStorage.removeItem('adminRefreshToken');
      localStorage.removeItem('adminUser');
    }
    this.currentAdminSubject.next(null);
  }

  /**
   * Check if admin is logged in
   */
  isLoggedIn(): boolean {
    if (isPlatformBrowser(this.platformId)) {
      return !!localStorage.getItem('adminToken');
    }
    return false;
  }

  /**
   * Get current admin
   */
  getCurrentAdmin(): Admin | null {
    return this.currentAdminSubject.value;
  }

  /**
   * Get admin token
   */
  getToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('adminToken');
    }
    return null;
  }

  // ============================================
  // STUDENT MANAGEMENT
  // ============================================

  /**
   * Get all students
   */
  getAllStudents(): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.apiUrl}/students`);
  }

  /**
   * Get student by ID
   */
  getStudentById(id: number): Observable<Student> {
    return this.http.get<Student>(`${this.apiUrl}/students/${id}`);
  }

  /**
   * Update student
   */
  updateStudent(id: number, data: UpdateStudentRequest): Observable<Student> {
    return this.http.put<Student>(`${this.apiUrl}/students/${id}`, data);
  }

  /**
   * Delete student
   */
  deleteStudent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/students/${id}`);
  }

  // ============================================
  // ADMIN MANAGEMENT
  // ============================================

  /**
   * Get all admins
   */
  getAllAdmins(): Observable<Admin[]> {
    return this.http.get<Admin[]>(`${this.apiUrl}/admins`);
  }

  /**
   * Create new admin
   */
  createAdmin(data: CreateAdminRequest): Observable<Admin> {
    return this.http.post<Admin>(`${this.apiUrl}/admins`, data);
  }

  /**
   * Activate admin
   */
  activateAdmin(id: number): Observable<Admin> {
    return this.http.put<Admin>(`${this.apiUrl}/admins/${id}/activate`, {});
  }

  /**
   * Deactivate admin
   */
  deactivateAdmin(id: number): Observable<Admin> {
    return this.http.put<Admin>(`${this.apiUrl}/admins/${id}/deactivate`, {});
  }

  // ============================================
  // DASHBOARD
  // ============================================

  /**
   * Get dashboard statistics
   */
  getDashboardStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${this.apiUrl}/dashboard/stats`);
  }
}