// src/app/services/announcement.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Announcement {
  id?: number;
  title: string;
  date: string;
  important: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class AnnouncementService {
  private apiUrl = `${environment.apiUrl}/announcements`;

  constructor(private http: HttpClient) {}

  /**
   * Get all announcements for a specific student
   */
  getStudentAnnouncements(studentId: number): Observable<Announcement[]> {
    return this.http.get<Announcement[]>(`${this.apiUrl}/student/${studentId}`);
  }

  /**
   * Get recent announcements for a student (default: 5 most recent)
   */
  getRecentAnnouncements(studentId: number, limit: number = 5): Observable<Announcement[]> {
    return this.http.get<Announcement[]>(`${this.apiUrl}/student/${studentId}/recent?limit=${limit}`);
  }

  /**
   * Get important announcements for a student
   */
  getImportantAnnouncements(studentId: number): Observable<Announcement[]> {
    return this.http.get<Announcement[]>(`${this.apiUrl}/student/${studentId}/important`);
  }

  /**
   * Get specific announcement by ID
   */
  getAnnouncementById(id: number): Observable<Announcement> {
    return this.http.get<Announcement>(`${this.apiUrl}/${id}`);
  }

  /**
   * Create new announcement for a student
   */
  createAnnouncement(studentId: number, announcement: Announcement): Observable<Announcement> {
    return this.http.post<Announcement>(`${this.apiUrl}/student/${studentId}`, announcement);
  }

  /**
   * Update announcement information
   */
  updateAnnouncement(id: number, announcement: Partial<Announcement>): Observable<Announcement> {
    return this.http.put<Announcement>(`${this.apiUrl}/${id}`, announcement);
  }

  /**
   * Delete an announcement
   */
  deleteAnnouncement(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  /**
   * Get all announcements (admin only)
   */
  getAllAnnouncements(): Observable<Announcement[]> {
    return this.http.get<Announcement[]>(this.apiUrl);
  }
}