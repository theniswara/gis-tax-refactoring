/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.psdku.lmj.university_backend.controller;

import com.psdku.lmj.university_backend.dto.AdminLoginRequest;
import com.psdku.lmj.university_backend.dto.CreateAdminRequest;
import com.psdku.lmj.university_backend.dto.LoginResponse;
import com.psdku.lmj.university_backend.dto.UpdateStudentRequest;
import com.psdku.lmj.university_backend.model.Admin;
import com.psdku.lmj.university_backend.model.Student;
import com.psdku.lmj.university_backend.security.RateLimitingService;
import com.psdku.lmj.university_backend.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin Controller - Admin-only endpoints
 * 
 * Security:
 * - All endpoints require ADMIN role
 * - Rate limiting on login
 * - Input validation
 * - Secure error messages
 * - CORS configured globally in SecurityConfig
 * 
 * IMPORTANT FIX:
 * Changed @RequestMapping from "/api/admin" to "/admin"
 * Because context-path is already "/api" in application.properties
 * Final URL: /api (context) + /admin (mapping) = /api/admin ✅
 */
@RestController
@RequestMapping("/admin")  // ✅ FIXED: Removed /api prefix (context-path handles it)
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private RateLimitingService rateLimitingService;
    
    /**
     * Admin login endpoint
     * Rate limited to prevent brute force attacks
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody AdminLoginRequest request,
            HttpServletRequest httpRequest) {
        
        String clientIp = getClientIp(httpRequest);
        
        // Rate limiting check
        if (!rateLimitingService.tryConsume(clientIp)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new LoginResponse(
                    false,
                    "Too many login attempts. Please try again later.",
                    null,
                    null,
                    null
                ));
        }
        
        LoginResponse response = adminService.login(request, clientIp);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    /**
     * Get all students
     * Requires ADMIN role
     */
    @GetMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = adminService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    
    /**
     * Get student by ID
     * Requires ADMIN role
     */
    @GetMapping("/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        return adminService.getStudentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Update student information
     * Requires ADMIN role
     */
    @PutMapping("/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStudentRequest request) {
        try {
            Student updatedStudent = adminService.updateStudent(id, request);
            return ResponseEntity.ok(updatedStudent);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Delete student account
     * Requires ADMIN role
     */
    @DeleteMapping("/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            adminService.deleteStudent(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Student deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Create new admin account
     * Requires ADMIN role
     */
    @PostMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody CreateAdminRequest request) {
        try {
            Admin newAdmin = adminService.createAdmin(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAdmin);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get all admins
     * Requires ADMIN role
     */
    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }
    
    /**
     * Deactivate admin account
     * Requires ADMIN role
     */
    @PutMapping("/admins/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivateAdmin(@PathVariable Long id) {
        try {
            adminService.deactivateAdmin(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Admin deactivated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Activate admin account
     * Requires ADMIN role
     */
    @PutMapping("/admins/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateAdmin(@PathVariable Long id) {
        try {
            adminService.activateAdmin(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Admin activated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get dashboard statistics
     * Requires ADMIN role
     */
    @GetMapping("/dashboard/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminService.AdminDashboardStats> getDashboardStats() {
        AdminService.AdminDashboardStats stats = adminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Extract client IP address from request
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
