// src/app/student/current-courses/current-courses.component.ts
import { Component, OnInit } from '@angular/core';
import { CourseService, Course, CourseStatistics } from '../../services/course.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-current-courses',
  templateUrl: './current-courses.component.html',
  styleUrls: ['./current-courses.component.css']
})
export class CurrentCoursesComponent implements OnInit {
  courses: Course[] = [];
  statistics: CourseStatistics | null = null;
  loading = false;
  error: string | null = null;
  studentId: number | null = null;

  constructor(
    private courseService: CourseService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadCurrentStudent();
  }

  loadCurrentStudent(): void {
    // Get current student from auth service
    // Adjust this based on your auth implementation
    const currentStudent = this.authService.getCurrentStudent();
    
    if (currentStudent && currentStudent.id) {
      this.studentId = currentStudent.id;
      this.loadCourses();
      this.loadStatistics();
    } else {
      this.error = 'Student information not found. Please log in again.';
    }
  }

  loadCourses(): void {
    if (!this.studentId) return;
    
    this.loading = true;
    this.error = null;
    
    this.courseService.getStudentCourses(this.studentId).subscribe({
      next: (courses) => {
        this.courses = courses;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading courses:', err);
        this.error = 'Failed to load courses. Please try again.';
        this.loading = false;
      }
    });
  }

  loadStatistics(): void {
    if (!this.studentId) return;
    
    this.courseService.getStudentCourseStatistics(this.studentId).subscribe({
      next: (stats) => {
        this.statistics = stats;
      },
      error: (err) => {
        console.error('Error loading statistics:', err);
      }
    });
  }

  getProgressColor(progress: number): string {
    if (progress >= 80) return 'bg-green-500';
    if (progress >= 50) return 'bg-blue-500';
    if (progress >= 30) return 'bg-yellow-500';
    return 'bg-gray-400';
  }

  getGradeColor(grade: string): string {
    if (grade.startsWith('A')) return 'text-green-600 bg-green-100';
    if (grade.startsWith('B')) return 'text-blue-600 bg-blue-100';
    if (grade.startsWith('C')) return 'text-yellow-600 bg-yellow-100';
    if (grade.startsWith('D')) return 'text-orange-600 bg-orange-100';
    return 'text-gray-600 bg-gray-100';
  }

  dropCourse(course: Course): void {
    if (!course.id) return;
    
    if (confirm(`Are you sure you want to drop ${course.name}?`)) {
      this.courseService.dropCourse(course.id).subscribe({
        next: () => {
          alert('Course dropped successfully');
          this.loadCourses();
          this.loadStatistics();
        },
        error: (err) => {
          console.error('Error dropping course:', err);
          alert('Failed to drop course. Please try again.');
        }
      });
    }
  }

  refreshCourses(): void {
    this.loadCourses();
    this.loadStatistics();
  }
}
