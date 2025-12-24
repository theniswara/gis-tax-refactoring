// ========================================
// STUDENT PORTAL COMPONENT - FIXED VERSION
// ========================================

// src/app/components/student-portal/student-portal.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { 
  LucideAngularModule, 
  GraduationCap, Award, BookOpen, Users, 
  CheckCircle, Clock, MapPin, Calendar, 
  FileText, Bell, TrendingUp, Globe 
} from 'lucide-angular';
import { AuthService } from '../../services/auth.service';
import { CourseService } from '../../services/course.service';
import { AssignmentService } from '../../services/assignment.service';
import { AnnouncementService } from '../../services/announcement.service';
import { Student, Course } from '../../models/student.model';

@Component({
  selector: 'app-student-portal',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule],
  templateUrl: './student-portal.component.html',
  styleUrls: ['./student-portal.component.css']
})
export class StudentPortalComponent implements OnInit, OnDestroy {
  // Icons
  readonly GraduationCap = GraduationCap;
  readonly Award = Award;
  readonly BookOpen = BookOpen;
  readonly Users = Users;
  readonly CheckCircle = CheckCircle;
  readonly Clock = Clock;
  readonly MapPin = MapPin;
  readonly Calendar = Calendar;
  readonly FileText = FileText;
  readonly Bell = Bell;
  readonly TrendingUp = TrendingUp;
  readonly Globe = Globe;

  // Auth state
  isAuthenticated = false;
  currentStudent: Student | null = null;

  // ✅ FIXED: Separate arrays for courses and assignments
  studentCourses: any[] = [];
  studentAssignments: any[] = [];
  studentAnnouncements: any[] = [];
  isLoadingCourses = false;
  isLoadingAssignments = false;
  isLoadingAnnouncements = false;

  // Login form
  studentId = '';
  password = '';
  loginError = '';
  isLoading = false;

  // Quick actions
  quickActions = [
    { title: 'Course Registration', icon: BookOpen, color: 'blue' },
    { title: 'Financial Services', icon: Award, color: 'green' },
    { title: 'Academic Calendar', icon: Calendar, color: 'purple' },
    { title: 'Library Resources', icon: BookOpen, color: 'orange' },
    { title: 'Career Services', icon: TrendingUp, color: 'red' },
    { title: 'IT Support', icon: Globe, color: 'indigo' }
  ];

  private destroy$ = new Subject<void>();

  constructor(
    private authService: AuthService,
    private courseService: CourseService,
    private assignmentService: AssignmentService,
    private announcementService: AnnouncementService
  ) {}

  ngOnInit(): void {
    // Subscribe to authentication state
    this.authService.isAuthenticated$
      .pipe(takeUntil(this.destroy$))
      .subscribe((isAuth: boolean) => {
        this.isAuthenticated = isAuth;
        
        // ✅ Load courses when authenticated
        if (isAuth && this.currentStudent) {
          this.loadStudentCourses();
          this.loadStudentAssignments();
          this.loadStudentAnnouncements();
        }
      });

    // Subscribe to current student data
    this.authService.currentStudent$
      .pipe(takeUntil(this.destroy$))
      .subscribe((student: Student | null) => {
        this.currentStudent = student;
        
        // ✅ Load courses when student data is available
        if (student && this.isAuthenticated) {
          this.loadStudentCourses();
          this.loadStudentAssignments();
          this.loadStudentAnnouncements();
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /**
   * ✅ NEW METHOD: Load student courses from API
   */
  private loadStudentCourses(): void {
    if (!this.currentStudent?.id) {
      console.warn('Cannot load courses: Student ID not available');
      return;
    }

    this.isLoadingCourses = true;
    
    // ✅ Convert string ID to number
    const studentId = Number(this.currentStudent.id);
    
    this.courseService.getStudentCourses(studentId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (courses) => {
          console.log('Courses loaded successfully:', courses);
          this.studentCourses = courses;
          this.isLoadingCourses = false;
          
          // Initialize currentCourses in student object for template compatibility
          if (this.currentStudent) {
            this.currentStudent.currentCourses = courses;
          }
        },
        error: (error) => {
          console.error('Error loading courses:', error);
          this.isLoadingCourses = false;
          this.studentCourses = [];
          
          // Initialize empty array for template compatibility
          if (this.currentStudent) {
            this.currentStudent.currentCourses = [];
            // Also initialize assignments and announcements if they don't exist
            if (!this.currentStudent.upcomingAssignments) {
              this.currentStudent.upcomingAssignments = [];
            }
            if (!this.currentStudent.announcements) {
              this.currentStudent.announcements = [];
            }
          }
        }
      });
  }

  /**
   * ✅ NEW METHOD: Load student assignments from API
   */
  private loadStudentAssignments(): void {
    if (!this.currentStudent?.id) {
      console.warn('Cannot load assignments: Student ID not available');
      return;
    }

    this.isLoadingAssignments = true;
    
    // Convert string ID to number
    const studentId = Number(this.currentStudent.id);
    
    this.assignmentService.getUpcomingAssignments(studentId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (assignments) => {
          console.log('Assignments loaded successfully:', assignments);
          this.studentAssignments = assignments;
          this.isLoadingAssignments = false;
          
          // Initialize upcomingAssignments in student object for template compatibility
          if (this.currentStudent) {
            this.currentStudent.upcomingAssignments = assignments;
          }
        },
        error: (error) => {
          console.error('Error loading assignments:', error);
          this.isLoadingAssignments = false;
          this.studentAssignments = [];
          
          // Initialize empty array for template compatibility
          if (this.currentStudent) {
            this.currentStudent.upcomingAssignments = [];
          }
        }
      });
  }

  /**
   * ✅ NEW METHOD: Load student announcements from API
   */
  private loadStudentAnnouncements(): void {
    if (!this.currentStudent?.id) {
      console.warn('Cannot load announcements: Student ID not available');
      return;
    }

    this.isLoadingAnnouncements = true;
    
    // Convert string ID to number
    const studentId = Number(this.currentStudent.id);
    
    this.announcementService.getRecentAnnouncements(studentId, 5)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (announcements) => {
          console.log('Announcements loaded successfully:', announcements);
          this.studentAnnouncements = announcements;
          this.isLoadingAnnouncements = false;
          
          // Initialize announcements in student object for template compatibility
          if (this.currentStudent) {
            this.currentStudent.announcements = announcements;
          }
        },
        error: (error) => {
          console.error('Error loading announcements:', error);
          this.isLoadingAnnouncements = false;
          this.studentAnnouncements = [];
          
          // Initialize empty array for template compatibility
          if (this.currentStudent) {
            this.currentStudent.announcements = [];
          }
        }
      });
  }

  // FIXED LOGIN METHOD
  onLogin(): void {
    this.loginError = '';
    this.isLoading = true;
    
    // Validate input
    if (!this.studentId || !this.password) {
      this.loginError = 'Please enter both Student ID and Password';
      this.isLoading = false;
      return;
    }

    // Use real API
    this.authService.login(this.studentId, this.password)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: any) => {
          this.isLoading = false;
          if (response.success) {
            console.log('Login successful:', response);
            // AuthService already updated the state via BehaviorSubjects
            // Courses will be loaded automatically via the observable subscription
          } else {
            this.loginError = response.message || 'Invalid credentials';
          }
        },
        error: (error) => {
          this.isLoading = false;
          console.error('Login error:', error);
          this.loginError = error.error?.message || 'Unable to connect to server. Please try again later.';
        }
      });
  }

  onLogout(): void {
    // Clear courses and assignments
    this.studentCourses = [];
    this.studentAssignments = [];
    
    // Logout through auth service
    this.authService.logout();
    
    // Clear form
    this.studentId = '';
    this.password = '';
    this.loginError = '';
  }

  getInitials(name: string): string {
    return name.split(' ').map(n => n[0]).join('');
  }

  getDaysUntilDue(dueDate: string): number {
    const due = new Date(dueDate);
    const now = new Date();
    const diff = due.getTime() - now.getTime();
    return Math.ceil(diff / (1000 * 60 * 60 * 24));
  }

  getProgressWidth(progress: number): string {
    return `${progress}%`;
  }

  viewCourseMaterials(course: Course): void {
    alert(`Opening materials for ${course.name}`);
  }

  viewAssignments(course: Course): void {
    alert(`Opening assignments for ${course.name}`);
  }
}