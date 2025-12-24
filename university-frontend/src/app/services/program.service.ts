/*
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ProgramService {

  constructor() { }
}
  */

// Update Program Service with HTTP
// src/app/services/program.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Program } from '../models/program.model';

@Injectable({
  providedIn: 'root'
})
export class ProgramService {
  private apiUrl = 'http://localhost:8080/api/programs';

  constructor(private http: HttpClient) {}

  getPrograms(): Observable<Program[]> {
    return this.http.get<Program[]>(this.apiUrl);
  }

  getProgramById(id: number): Observable<Program> {
    return this.http.get<Program>(`${this.apiUrl}/${id}`);
  }

  searchPrograms(query: string, degree: string, field: string): Observable<Program[]> {
    let params: any = {};
    if (query) params.query = query;
    if (degree && degree !== 'all') params.degree = degree;
    if (field && field !== 'all') params.field = field;
    
    return this.http.get<Program[]>(`${this.apiUrl}/search`, { params });
  }

  createProgram(program: Program): Observable<Program> {
    return this.http.post<Program>(this.apiUrl, program);
  }

  updateProgram(id: number, program: Program): Observable<Program> {
    return this.http.put<Program>(`${this.apiUrl}/${id}`, program);
  }

  deleteProgram(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
