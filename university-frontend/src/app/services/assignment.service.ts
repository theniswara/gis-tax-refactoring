// src/app/services/assignment.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Assignment {
  id?: number;
  course: string;
  title: string;
  dueDate: string;
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class AssignmentService {
  private apiUrl = `${environment.apiUrl}/assignments`;

  constructor(private http: HttpClient) {}

  /**
   * Get all assignments for a specific student
   */
  getStudentAssignments(studentId: number): Observable<Assignment[]> {
    return this.http.get<Assignment[]>(`${this.apiUrl}/student/${studentId}`);
  }

  /**
   * Get upcoming assignments for a student (due date >= today)
   */
  getUpcomingAssignments(studentId: number): Observable<Assignment[]> {
    return this.http.get<Assignment[]>(`${this.apiUrl}/student/${studentId}/upcoming`);
  }

  /**
   * Get assignments by status for a student
   * Status values: "Pending", "In Progress", "Completed", "Overdue"
   */
  getAssignmentsByStatus(studentId: number, status: string): Observable<Assignment[]> {
    return this.http.get<Assignment[]>(`${this.apiUrl}/student/${studentId}/status/${status}`);
  }

  /**
   * Get specific assignment by ID
   */
  getAssignmentById(id: number): Observable<Assignment> {
    return this.http.get<Assignment>(`${this.apiUrl}/${id}`);
  }

  /**
   * Create new assignment for a student
   */
  createAssignment(studentId: number, assignment: Assignment): Observable<Assignment> {
    return this.http.post<Assignment>(`${this.apiUrl}/student/${studentId}`, assignment);
  }

  /**
   * Update assignment information
   */
  updateAssignment(id: number, assignment: Partial<Assignment>): Observable<Assignment> {
    return this.http.put<Assignment>(`${this.apiUrl}/${id}`, assignment);
  }

  /**
   * Delete an assignment
   */
  deleteAssignment(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  /**
   * Get all assignments (admin only)
   */
  getAllAssignments(): Observable<Assignment[]> {
    return this.http.get<Assignment[]>(this.apiUrl);
  }
}