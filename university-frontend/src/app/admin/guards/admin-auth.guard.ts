import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AdminService } from '../services/admin.service';

/**
 * Admin Auth Guard - Protects admin routes
 * Only allows access if admin is logged in
 */
export const adminAuthGuard: CanActivateFn = (route, state) => {
  const adminService = inject(AdminService);
  const router = inject(Router);

  if (adminService.isLoggedIn()) {
    return true;
  }

  // Redirect to admin login
  router.navigate(['/admin/login'], { 
    queryParams: { returnUrl: state.url } 
  });
  return false;
};

/**
 * Admin Login Guard - Prevents logged-in admins from accessing login page
 */
export const adminLoginGuard: CanActivateFn = (route, state) => {
  const adminService = inject(AdminService);
  const router = inject(Router);

  if (!adminService.isLoggedIn()) {
    return true;
  }

  // Already logged in, redirect to dashboard
  router.navigate(['/admin/dashboard']);
  return false;
};