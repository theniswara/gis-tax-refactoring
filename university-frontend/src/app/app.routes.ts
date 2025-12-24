import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { ProgramsComponent } from './components/programs/programs.component';
import { AdmissionsComponent } from './components/admissions/admissions.component';
import { ResearchComponent } from './components/research/research.component';
import { CampusLifeComponent } from './components/campus-life/campus-life.component';
import { NewsEventsComponent } from './components/news-events/news-events.component';
import { AboutComponent } from './components/about/about.component';
import { StudentPortalComponent } from './components/student-portal/student-portal.component';

// Import admin guards
import { adminAuthGuard, adminLoginGuard } from './admin/guards/admin-auth.guard';

export const routes: Routes = [
  // ============================================
  // PUBLIC ROUTES
  // ============================================
  { path: '', component: HomeComponent },
  { path: 'programs', component: ProgramsComponent },
  { path: 'programs/:id', component: ProgramsComponent },
  { path: 'admissions', component: AdmissionsComponent },
  { path: 'research', component: ResearchComponent },
  { path: 'campus-life', component: CampusLifeComponent },
  { path: 'news-events', component: NewsEventsComponent },
  { path: 'about', component: AboutComponent },
  { path: 'portal', component: StudentPortalComponent },

  // ============================================
  // ADMIN ROUTES
  // ============================================
  
  // Admin Login (public, but redirects if already logged in)
  {
    path: 'admin/login',
    loadComponent: () => import('./admin/components/admin-login/admin-login.component')
      .then(m => m.AdminLoginComponent),
    canActivate: [adminLoginGuard] // Prevents logged-in admins from accessing login
  },
  
  // Admin Panel (protected by adminAuthGuard)
  {
    path: 'admin',
    loadComponent: () => import('./admin/components/admin-layout/admin-layout.component')
      .then(m => m.AdminLayoutComponent),
    canActivate: [adminAuthGuard], // Only admins can access
    children: [
      // Default redirect to dashboard
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      // Dashboard
      {
        path: 'dashboard',
        loadComponent: () => import('./admin/components/admin-dashboard/admin-dashboard.component')
          .then(m => m.AdminDashboardComponent)
      },
      // Student Management
      {
        path: 'students',
        loadComponent: () => import('./admin/components/student-management/student-management.component')
          .then(m => m.StudentManagementComponent)
      },
      // Admin Management
      {
        path: 'admins',
        loadComponent: () => import('./admin/components/admin-management/admin-management.component')
          .then(m => m.AdminManagementComponent)
      }
    ]
  },

  // ============================================
  // FALLBACK - Must be last
  // ============================================
  { path: '**', redirectTo: '' }
];