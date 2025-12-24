import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { Admin, CreateAdminRequest } from '../../models/admin.model';

@Component({
  selector: 'app-admin-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-management.component.html',
  styleUrls: ['./admin-management.component.css']
})
export class AdminManagementComponent implements OnInit {
  admins: Admin[] = [];
  isLoading = true;
  errorMessage = '';

  // Create modal
  isCreateModalOpen = false;
  createForm: CreateAdminRequest = {
    username: '',
    email: '',
    password: '',
    fullName: ''
  };
  isCreating = false;
  createError = '';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadAdmins();
  }

  loadAdmins(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.adminService.getAllAdmins().subscribe({
      next: (data) => {
        this.admins = data;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading admins:', error);
        this.errorMessage = 'Failed to load administrators';
        this.isLoading = false;
      }
    });
  }

  openCreateModal(): void {
    this.createForm = {
      username: '',
      email: '',
      password: '',
      fullName: ''
    };
    this.isCreateModalOpen = true;
    this.createError = '';
  }

  closeCreateModal(): void {
    this.isCreateModalOpen = false;
    this.createForm = {
      username: '',
      email: '',
      password: '',
      fullName: ''
    };
    this.createError = '';
  }

  createAdmin(): void {
    // Validation
    if (!this.createForm.username || !this.createForm.email || 
        !this.createForm.password || !this.createForm.fullName) {
      this.createError = 'All fields are required';
      return;
    }

    if (this.createForm.password.length < 8) {
      this.createError = 'Password must be at least 8 characters';
      return;
    }

    this.isCreating = true;
    this.createError = '';

    this.adminService.createAdmin(this.createForm).subscribe({
      next: (newAdmin) => {
        // Add new admin to list
        this.admins.push(newAdmin);
        this.isCreating = false;
        this.closeCreateModal();
      },
      error: (error) => {
        console.error('Error creating admin:', error);
        
        if (error.status === 409) {
          this.createError = 'Username or email already exists';
        } else {
          this.createError = error.error?.message || 'Failed to create admin';
        }
        
        this.isCreating = false;
      }
    });
  }

  toggleAdminStatus(admin: Admin): void {
    const action = admin.isActive ? 'deactivate' : 'activate';
    const confirmMessage = admin.isActive 
      ? `Are you sure you want to deactivate ${admin.fullName}? They will not be able to login.`
      : `Are you sure you want to activate ${admin.fullName}?`;

    if (!confirm(confirmMessage)) {
      return;
    }

    const operation = admin.isActive 
      ? this.adminService.deactivateAdmin(admin.id)
      : this.adminService.activateAdmin(admin.id);

    operation.subscribe({
      next: (updatedAdmin) => {
        // Update admin in list
        const index = this.admins.findIndex(a => a.id === updatedAdmin.id);
        if (index !== -1) {
          this.admins[index] = updatedAdmin;
        }
      },
      error: (error) => {
        console.error(`Error ${action}ing admin:`, error);
        alert(`Failed to ${action} admin`);
      }
    });
  }

  getStatusBadgeClass(admin: Admin): string {
    return admin.isActive ? 'badge-active' : 'badge-inactive';
  }

  getStatusText(admin: Admin): string {
    return admin.isActive ? 'Active' : 'Inactive';
  }
}