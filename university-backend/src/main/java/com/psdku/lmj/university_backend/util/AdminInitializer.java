/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.psdku.lmj.university_backend.util;

import com.psdku.lmj.university_backend.model.Admin;
import com.psdku.lmj.university_backend.model.Role;
import com.psdku.lmj.university_backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Admin Account Initializer
 * 
 * Automatically creates default admin account on application startup
 * if it doesn't exist.
 * 
 * Default credentials:
 * Username: admin
 * Password: admin123
 * 
 * This ensures the password is encoded correctly using the
 * application's PasswordEncoder bean.
 */
@Component
public class AdminInitializer implements CommandLineRunner {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        createDefaultAdminIfNotExists();
    }
    
    private void createDefaultAdminIfNotExists() {
        String defaultUsername = "admin";
        String defaultPassword = "admin123";
        
        // Check if admin already exists
        if (adminRepository.findByUsername(defaultUsername).isEmpty()) {
            System.out.println("=================================");
            System.out.println("Creating default admin account...");
            System.out.println("=================================");
            
            Admin admin = new Admin();
            admin.setUsername(defaultUsername);
            admin.setEmail("admin@university.edu");
            
            // Use PasswordEncoder to hash password - GUARANTEED to work!
            admin.setPassword(passwordEncoder.encode(defaultPassword));
            
            admin.setFullName("System Administrator");
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            
            adminRepository.save(admin);
            
            System.out.println("✅ Default admin account created!");
            System.out.println("   Username: " + defaultUsername);
            System.out.println("   Password: " + defaultPassword);
            System.out.println("   Status: Active");
            System.out.println("=================================");
        } else {
            System.out.println("ℹ️  Admin account already exists - skipping creation");
        }
    }
}
