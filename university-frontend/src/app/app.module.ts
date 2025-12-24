import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// Interceptors
import { AuthInterceptor } from './interceptors/auth.interceptor';

// Services
import { AuthService } from './services/auth.service';
import { ProgramService } from './services/program.service';
import { CourseService } from './services/course.service';
import { AdminService } from './admin/services/admin.service';

// Public Components
import { HomeComponent } from './home/home.component';
import { ProgramsComponent } from './programs/programs.component';
import { NewsComponent } from './news/news.component';
import { EventsComponent } from './events/events.component';

// Student Components
import { StudentLoginComponent } from './student/student-login/student-login.component';
import { StudentDashboardComponent } from './student/student-dashboard/student-dashboard.component';
import { StudentProfileComponent } from './student/student-profile/student-profile.component';
import { CurrentCoursesComponent } from './student/current-courses/current-courses.component';

// Admin Components
import { AdminLoginComponent } from './admin/admin-login/admin-login.component';
import { AdminDashboardComponent } from './admin/admin-dashboard/admin-dashboard.component';
import { StudentManagementComponent } from './admin/student-management/student-management.component';
import { AdminManagementComponent } from './admin/admin-management/admin-management.component';

// Shared Components
import { NavbarComponent } from './shared/navbar/navbar.component';
import { FooterComponent } from './shared/footer/footer.component';

/**
 * Main Application Module
 * 
 * This module contains all the components, services, and configurations
 * for the University Management System.
 * 
 * Features:
 * - Student Portal (login, dashboard, courses, profile)
 * - Admin Panel (login, dashboard, student management, admin management)
 * - Public Pages (home, programs, news, events)
 * - JWT Authentication with Interceptor
 * - HTTP Client for API calls
 * - Reactive Forms for form handling
 */
@NgModule({
  declarations: [
    // Root Component
    AppComponent,
    
    // ============================================
    // PUBLIC COMPONENTS
    // ============================================
    HomeComponent,
    ProgramsComponent,
    NewsComponent,
    EventsComponent,
    
    // ============================================
    // STUDENT COMPONENTS
    // ============================================
    StudentLoginComponent,
    StudentDashboardComponent,
    StudentProfileComponent,
    CurrentCoursesComponent,
    
    // ============================================
    // ADMIN COMPONENTS
    // ============================================
    AdminLoginComponent,
    AdminDashboardComponent,
    StudentManagementComponent,
    AdminManagementComponent,
    
    // ============================================
    // SHARED COMPONENTS
    // ============================================
    NavbarComponent,
    FooterComponent
  ],
  imports: [
    // Angular Core Modules
    BrowserModule,
    AppRoutingModule,
    CommonModule,
    
    // Forms Modules (Template-driven and Reactive)
    FormsModule,
    ReactiveFormsModule,
    
    // HTTP Client Module for API calls
    HttpClientModule
  ],
  providers: [
    // ============================================
    // SERVICES
    // ============================================
    AuthService,
    ProgramService,
    CourseService,
    AdminService,
    
    // ============================================
    // HTTP INTERCEPTORS
    // ============================================
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }