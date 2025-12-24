// src/app/guards/admin.guard.ts
import { Injectable, inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AdminService } from '../admin/services/admin.service';

/**
 * Admin Guard
 * 
 * Protects admin routes from unauthorized access.
 * Only allows users with admin authentication.
 * Redirects to admin login page if user is not authenticated as admin.
 * 
 * Usage:
 * In routing: canActivate: [AdminGuard]
 */
@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  private platformId = inject(PLATFORM_ID);

  constructor(
    private router: Router,
    private adminService: AdminService
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    // Check if running in browser (not SSR)
    if (!isPlatformBrowser(this.platformId)) {
      return false;
    }

    // Check if admin is authenticated
    const isAdminAuthenticated = this.adminService.isAdminAuthenticated();

    if (isAdminAuthenticated) {
      return true;
    }

    // Not authenticated as admin, redirect to admin login
    this.router.navigate(['/admin/login'], {
      queryParams: { returnUrl: state.url }
    });
    return false;
  }
}