/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.psdku.lmj.university_backend.service;

import com.psdku.lmj.university_backend.dto.AdminLoginRequest;
import com.psdku.lmj.university_backend.dto.CreateAdminRequest;
import com.psdku.lmj.university_backend.dto.LoginResponse;
import com.psdku.lmj.university_backend.dto.UpdateStudentRequest;
import com.psdku.lmj.university_backend.model.Admin;
import com.psdku.lmj.university_backend.model.Role;
import com.psdku.lmj.university_backend.model.Student;
import com.psdku.lmj.university_backend.repository.AdminRepository;
import com.psdku.lmj.university_backend.repository.StudentRepository;
import com.psdku.lmj.university_backend.security.AccountLockoutService;
import com.psdku.lmj.university_backend.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Admin Service - Business logic for admin operations
 * 
 * Features:
 * - Admin authentication
 * - User management (CRUD)
 * - Student account management
 * - BCrypt password hashing
 * - Account lockout integration
 */
@Service
public class AdminService {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenService jwtTokenService;
    
    @Autowired
    private AccountLockoutService accountLockoutService;
    
    /**
     * Admin login with security features
     */
    public LoginResponse login(AdminLoginRequest request, String ipAddress) {
        // Check if account is locked
        if (accountLockoutService.isLocked(request.getUsername())) {
            int remainingTime = accountLockoutService.getRemainingLockoutTime(request.getUsername());
            return new LoginResponse(
                false, 
                "Account locked. Try again in " + remainingTime + " minutes.",
                null, 
                null, 
                null
            );
        }
        
        // Find admin by username
        Optional<Admin> adminOpt = adminRepository.findActiveByUsername(request.getUsername());
        
        if (adminOpt.isEmpty()) {
            accountLockoutService.recordFailedAttempt(request.getUsername());
            return new LoginResponse(
                false,
                "Invalid credentials.",
                null,
                null,
                null
            );
        }
        
        Admin admin = adminOpt.get();
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            accountLockoutService.recordFailedAttempt(request.getUsername());
            int remainingAttempts = accountLockoutService.getRemainingAttempts(request.getUsername());
            return new LoginResponse(
                false,
                "Invalid credentials. " + remainingAttempts + " attempts remaining.",
                null,
                null,
                null
            );
        }
        
        // Reset failed attempts on successful login
        accountLockoutService.resetFailedAttempts(request.getUsername());
        
        // Update last login
        admin.setLastLogin(LocalDateTime.now());
        adminRepository.save(admin);
        
        // Generate JWT tokens
        String accessToken = jwtTokenService.generateAccessToken(admin.getUsername());
        String refreshToken = jwtTokenService.generateRefreshToken(admin.getUsername());
        
        return new LoginResponse(
            true,
            "Login successful",
            accessToken,
            refreshToken,
            admin
        );
    }
    
    /**
     * Create new admin account
     * Only accessible by existing admins
     */
    @Transactional
    public Admin createAdmin(CreateAdminRequest request) {
        // Check if username exists
        if (adminRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email exists
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Create admin
        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));  // Hash password
        admin.setFullName(request.getFullName());
        admin.setRole(Role.ADMIN);
        admin.setActive(true);
        
        return adminRepository.save(admin);
    }
    
    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    /**
     * Get student by ID
     */
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
    
    /**
     * Update student information
     */
    @Transactional
    public Student updateStudent(Long id, UpdateStudentRequest request) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        // Update fields if provided
        if (request.getName() != null && !request.getName().isEmpty()) {
            student.setName(request.getName());
        }
        
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            // Check if email already exists for another student
            Optional<Student> existingStudent = studentRepository.findByEmail(request.getEmail());
            if (existingStudent.isPresent() && !existingStudent.get().getId().equals(id)) {
                throw new RuntimeException("Email already exists");
            }
            student.setEmail(request.getEmail());
        }
        
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            student.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        if (request.getMajor() != null) {
            student.setMajor(request.getMajor());
        }
        
        if (request.getPhoneNumber() != null) {
            student.setPhoneNumber(request.getPhoneNumber());
        }
        
        return studentRepository.save(student);
    }
    
    /**
     * Delete student account
     */
    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found");
        }
        studentRepository.deleteById(id);
    }
    
    /**
     * Get all admins
     */
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    
    /**
     * Deactivate admin account
     */
    @Transactional
    public void deactivateAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Admin not found"));
        admin.setActive(false);
        adminRepository.save(admin);
    }
    
    /**
     * Activate admin account
     */
    @Transactional
    public void activateAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Admin not found"));
        admin.setActive(true);
        adminRepository.save(admin);
    }
    
    /**
     * Get dashboard statistics
     */
    public AdminDashboardStats getDashboardStats() {
        long totalStudents = studentRepository.count();
        long totalAdmins = adminRepository.count();
        
        return new AdminDashboardStats(totalStudents, totalAdmins);
    }
    
    /**
     * Inner class for dashboard statistics
     */
    public static class AdminDashboardStats {
        private long totalStudents;
        private long totalAdmins;
        
        public AdminDashboardStats(long totalStudents, long totalAdmins) {
            this.totalStudents = totalStudents;
            this.totalAdmins = totalAdmins;
        }
        
        public long getTotalStudents() {
            return totalStudents;
        }
        
        public long getTotalAdmins() {
            return totalAdmins;
        }
    }
}
