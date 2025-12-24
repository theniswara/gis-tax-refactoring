/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.psdku.lmj.university_backend.util;

import com.psdku.lmj.university_backend.model.Student;
import com.psdku.lmj.university_backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Password Migration Utility
 * 
 * IMPORTANT: This is a ONE-TIME utility to hash existing plain-text passwords
 * 
 * Usage:
 * 1. Uncomment the @Component annotation below
 * 2. Run the application once
 * 3. Comment out the @Component annotation again
 * 4. Delete or disable this file
 * 
 * WARNING: This will modify all passwords in the database!
 */
//@Component // ⚠️ UNCOMMENT THIS ONLY WHEN YOU WANT TO RUN THE MIGRATION
public class PasswordMigrationUtil implements CommandLineRunner {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("========================================");
        System.out.println("STARTING PASSWORD MIGRATION");
        System.out.println("========================================");
        
        List<Student> students = studentRepository.findAll();
        int count = 0;
        
        for (Student student : students) {
            String currentPassword = student.getPassword();
            
            // Check if password is already hashed (BCrypt hashes start with $2a$, $2b$, or $2y$)
            if (currentPassword != null && !currentPassword.startsWith("$2")) {
                System.out.println("Migrating password for student: " + student.getStudentId());
                
                // Hash the plain text password
                String hashedPassword = passwordEncoder.encode(currentPassword);
                student.setPassword(hashedPassword);
                
                studentRepository.save(student);
                count++;
            }
        }
        
        System.out.println("========================================");
        System.out.println("PASSWORD MIGRATION COMPLETE");
        System.out.println("Total passwords migrated: " + count);
        System.out.println("========================================");
        System.out.println("⚠️  IMPORTANT: Comment out @Component annotation in PasswordMigrationUtil.java");
        System.out.println("========================================");
    }
}
