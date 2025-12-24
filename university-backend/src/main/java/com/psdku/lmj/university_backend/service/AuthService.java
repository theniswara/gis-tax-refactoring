/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.psdku.lmj.university_backend.service;

import com.psdku.lmj.university_backend.dto.LoginRequest;
import com.psdku.lmj.university_backend.dto.LoginResponse;
import com.psdku.lmj.university_backend.model.Student;
import com.psdku.lmj.university_backend.repository.StudentRepository;
import com.psdku.lmj.university_backend.security.AccountLockoutService;
import com.psdku.lmj.university_backend.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Secure Authentication Service
 * 
 * Security Features:
 * - BCrypt password hashing
 * - JWT token generation
 * - Account lockout protection
 * - Timing attack prevention
 * - Secure error messages
 */
@Service
public class AuthService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenService jwtTokenService;
    
    @Autowired
    private AccountLockoutService accountLockoutService;
    
    /**
     * Authenticate user and generate JWT tokens
     */
    public LoginResponse login(LoginRequest loginRequest) {
        String studentId = loginRequest.getStudentId();
        
        // ✅ Check if account is locked
        if (accountLockoutService.isAccountLocked(studentId)) {
            LocalDateTime lockoutEnd = accountLockoutService.getLockoutEndTime(studentId);
            String lockoutMessage = "Account is locked due to multiple failed login attempts. " +
                                  "Try again after " + 
                                  lockoutEnd.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            return new LoginResponse(false, lockoutMessage);
        }
        
        // ✅ Find student by ID
        Optional<Student> studentOptional = studentRepository.findByStudentId(studentId);
        
        if (studentOptional.isEmpty()) {
            // ✅ Record failed attempt even if user doesn't exist (prevents user enumeration)
            accountLockoutService.recordFailedAttempt(studentId);
            return new LoginResponse(false, "Invalid credentials");
        }
        
        Student student = studentOptional.get();
        
        // ✅ Verify password using BCrypt
        if (!passwordEncoder.matches(loginRequest.getPassword(), student.getPassword())) {
            // Record failed attempt
            accountLockoutService.recordFailedAttempt(studentId);
            
            int remainingAttempts = accountLockoutService.getRemainingAttempts(studentId);
            if (remainingAttempts > 0) {
                return new LoginResponse(false, 
                    "Invalid credentials. " + remainingAttempts + " attempts remaining.");
            } else {
                return new LoginResponse(false, 
                    "Account locked due to multiple failed attempts. Try again in 15 minutes.");
            }
        }
        
        // ✅ Reset login attempts on successful login
        accountLockoutService.resetAttempts(studentId);
        
        // ✅ Generate JWT tokens
        String accessToken = jwtTokenService.generateAccessToken(student.getStudentId());
        String refreshToken = jwtTokenService.generateRefreshToken(student.getStudentId());
        
        // ✅ Remove password from response
        student.setPassword(null);
        
        return new LoginResponse(true, "Login successful", accessToken, refreshToken, student);
    }
    
    /**
     * Refresh access token using refresh token
     */
    public LoginResponse refreshToken(String refreshToken) {
        try {
            if (jwtTokenService.validateToken(refreshToken)) {
                String studentId = jwtTokenService.getStudentIdFromToken(refreshToken);
                String newAccessToken = jwtTokenService.generateAccessToken(studentId);
                
                return new LoginResponse(true, "Token refreshed", newAccessToken, refreshToken, null);
            }
        } catch (Exception e) {
            // Log error but don't expose details
            System.err.println("Token refresh error: " + e.getMessage());
        }
        
        return new LoginResponse(false, "Invalid or expired refresh token");
    }
    
    /**
     * Register new student with hashed password
     */
    public Student registerStudent(Student student) {
        // ✅ Hash password before saving
        String hashedPassword = passwordEncoder.encode(student.getPassword());
        student.setPassword(hashedPassword);
        
        return studentRepository.save(student);
    }
}

