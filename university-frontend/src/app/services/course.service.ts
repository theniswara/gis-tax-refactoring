// src/app/services/course.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Course {
  id?: number;
  code: string;
  name: string;
  instructor: string;
  grade: string;
  progress: number;
  credits: number;
  schedule: string;
  room: string;
}

export interface CourseStatistics {
  totalCourses: number;
  totalCredits: number;
  averageProgress: number;
  completedCourses: number;
}

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  private apiUrl = `${environment.apiUrl}/courses`;

  constructor(private http: HttpClient) {}

  /**
   * Get all courses for a specific student
   */
  getStudentCourses(studentId: number): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.apiUrl}/student/${studentId}`);
  }

  /**
   * Get course statistics for a student
   */
  getStudentCourseStatistics(studentId: number): Observable<CourseStatistics> {
    return this.http.get<CourseStatistics>(`${this.apiUrl}/student/${studentId}/statistics`);
  }

  /**
   * Get specific course by ID
   */
  getCourseById(id: number): Observable<Course> {
    return this.http.get<Course>(`${this.apiUrl}/${id}`);
  }

  /**
   * Enroll student in a course
   */
  enrollStudentInCourse(studentId: number, course: Course): Observable<Course> {
    return this.http.post<Course>(`${this.apiUrl}/student/${studentId}`, course);
  }

  /**
   * Update course information
   */
  updateCourse(id: number, course: Partial<Course>): Observable<Course> {
    return this.http.put<Course>(`${this.apiUrl}/${id}`, course);
  }

  /**
   * Drop a course
   */
  dropCourse(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  /**
   * Get all courses (admin only)
   */
  getAllCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(this.apiUrl);
  }
}