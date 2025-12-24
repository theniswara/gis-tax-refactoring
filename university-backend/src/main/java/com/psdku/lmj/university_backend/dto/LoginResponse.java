/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.psdku.lmj.university_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login Response DTO
 * 
 * Generic response that can contain either Student or Admin data
 * Contains:
 * - Success status
 * - Message
 * - JWT access token
 * - JWT refresh token
 * - User data (Student or Admin, without password)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    private boolean success;
    private String message;
    private String accessToken;
    private String refreshToken;
    private Object user;  // Can be Student or Admin
    
    // Constructor for error responses
    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.accessToken = null;
        this.refreshToken = null;
        this.user = null;
    }
    
    // Backward compatibility: student field that returns user
    public Object getStudent() {
        return user;
    }
    
    public void setStudent(Object student) {
        this.user = student;
    }
}
