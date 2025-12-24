/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.psdku.lmj.university_backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Password Hash Generator
 * 
 * Run this to generate a BCrypt hash for admin password
 * Usage: Right-click -> Run As -> Java Application
 */
public class PasswordHashGenerator {
    
    public static void main(String[] args) {
        // Create encoder with same strength as SecurityConfig
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        
        // Generate hash for 'admin123'
        String password = "admin123";
        String hash = encoder.encode(password);
        
        System.out.println("=================================");
        System.out.println("PASSWORD HASH GENERATOR");
        System.out.println("=================================");
        System.out.println("Password: " + password);
        System.out.println("Hash: " + hash);
        System.out.println("=================================");
        System.out.println("\nCopy this hash into your SQL:");
        System.out.println("\nUPDATE admins SET password = '" + hash + "' WHERE username = 'admin';");
        System.out.println("\n=================================");
        
        // Test the hash
        boolean matches = encoder.matches(password, hash);
        System.out.println("Test: " + (matches ? "✅ Hash is valid" : "❌ Hash is invalid"));
        
        // Test with the hash we've been using
        String oldHash = "$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5nduLbZ4jJGWu";
        boolean oldMatches = encoder.matches(password, oldHash);
        System.out.println("Old hash test: " + (oldMatches ? "✅ Old hash works" : "❌ Old hash doesn't work"));
    }
}
