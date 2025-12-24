import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { Student, UpdateStudentRequest } from '../../models/student.model';

@Component({
  selector: 'app-student-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student-management.component.html',
  styleUrls: ['./student-management.component.css']
})
export class StudentManagementComponent implements OnInit {
  students: Student[] = [];
  filteredStudents: Student[] = [];
  searchTerm: string = '';
  isLoading = true;
  errorMessage = '';

  // Edit modal
  isEditModalOpen = false;
  editingStudent: Student | null = null;
  editForm: UpdateStudentRequest = {};
  isSaving = false;
  saveError = '';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadStudents();
  }

  loadStudents(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.adminService.getAllStudents().subscribe({
      next: (data) => {
        this.students = data;
        this.filteredStudents = data;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading students:', error);
        this.errorMessage = 'Failed to load students';
        this.isLoading = false;
      }
    });
  }

  onSearch(): void {
    const term = this.searchTerm.toLowerCase().trim();
    
    if (!term) {
      this.filteredStudents = this.students;
      return;
    }

    this.filteredStudents = this.students.filter(student =>
      student.name.toLowerCase().includes(term) ||
      student.studentId.toLowerCase().includes(term) ||
      student.email.toLowerCase().includes(term) ||
      (student.major && student.major.toLowerCase().includes(term))
    );
  }

  openEditModal(student: Student): void {
    this.editingStudent = student;
    this.editForm = {
      name: student.name,
      email: student.email,
      major: student.major || '',
      phoneNumber: student.phoneNumber || ''
    };
    this.isEditModalOpen = true;
    this.saveError = '';
  }

  closeEditModal(): void {
    this.isEditModalOpen = false;
    this.editingStudent = null;
    this.editForm = {};
    this.saveError = '';
  }

  saveStudent(): void {
    if (!this.editingStudent) return;

    this.isSaving = true;
    this.saveError = '';

    this.adminService.updateStudent(this.editingStudent.id, this.editForm).subscribe({
      next: (updatedStudent) => {
        // Update student in list
        const index = this.students.findIndex(s => s.id === updatedStudent.id);
        if (index !== -1) {
          this.students[index] = updatedStudent;
          this.filteredStudents = [...this.students];
          this.onSearch(); // Re-apply search filter
        }
        
        this.isSaving = false;
        this.closeEditModal();
      },
      error: (error) => {
        console.error('Error updating student:', error);
        this.saveError = error.error?.message || 'Failed to update student';
        this.isSaving = false;
      }
    });
  }

  deleteStudent(student: Student): void {
    if (!confirm(`Are you sure you want to delete ${student.name}?`)) {
      return;
    }

    this.adminService.deleteStudent(student.id).subscribe({
      next: () => {
        // Remove student from list
        this.students = this.students.filter(s => s.id !== student.id);
        this.filteredStudents = this.filteredStudents.filter(s => s.id !== student.id);
      },
      error: (error) => {
        console.error('Error deleting student:', error);
        alert('Failed to delete student');
      }
    });
  }
}