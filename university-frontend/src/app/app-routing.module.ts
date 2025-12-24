import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// Guards
import { AuthGuard } from './guards/auth.guard';
import { AdminGuard } from './guards/admin.guard';

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

/**
 * Application Routes
 * 
 * Route Structure:
 * - / → Home (public)
 * - /programs → Programs list (public)
 * - /news → News list (public)
 * - /events → Events list (public)
 * - /login → Student login (public)
 * - /portal → Student portal (protected by AuthGuard)
 *   - /portal/dashboard → Student dashboard
 *   - /portal/courses → Current courses
 *   - /portal/profile → Student profile
 * - /admin/login → Admin login (public)
 * - /admin → Admin panel (protected by AdminGuard)
 *   - /admin/dashboard → Admin dashboard
 *   - /admin/students → Student management
 *   - /admin/admins → Admin management
 */
const routes: Routes = [
  // ============================================
  // PUBLIC ROUTES
  // ============================================
  {
    path: '',
    component: HomeComponent,
    pathMatch: 'full'
  },
  {
    path: 'programs',
    component: ProgramsComponent
  },
  {
    path: 'news',
    component: NewsComponent
  },
  {
    path: 'events',
    component: EventsComponent
  },
  
  // ============================================
  // STUDENT ROUTES
  // ============================================
  {
    path: 'login',
    component: StudentLoginComponent
  },
  {
    path: 'portal',
    canActivate: [AuthGuard],
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        component: StudentDashboardComponent
      },
      {
        path: 'courses',
        component: CurrentCoursesComponent
      },
      {
        path: 'profile',
        component: StudentProfileComponent
      }
    ]
  },
  
  // ============================================
  // ADMIN ROUTES
  // ============================================
  {
    path: 'admin/login',
    component: AdminLoginComponent
  },
  {
    path: 'admin',
    canActivate: [AdminGuard],
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        component: AdminDashboardComponent
      },
      {
        path: 'students',
        component: StudentManagementComponent
      },
      {
        path: 'admins',
        component: AdminManagementComponent
      }
    ]
  },
  
  // ============================================
  // WILDCARD ROUTE (404)
  // ============================================
  {
    path: '**',
    redirectTo: ''
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }