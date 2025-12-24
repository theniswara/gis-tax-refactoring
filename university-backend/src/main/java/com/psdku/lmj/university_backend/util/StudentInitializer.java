/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.psdku.lmj.university_backend.util;

import com.psdku.lmj.university_backend.model.Role;
import com.psdku.lmj.university_backend.model.Student;
import com.psdku.lmj.university_backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Student Account Initializer
 * 
 * Automatically creates default test student account on application startup
 * if it doesn't exist.
 * 
 * Default credentials:
 * Student ID: STU0000000001
 * Password: student123
 */
@Component
@Order(2)  // Run after AdminInitializer
public class StudentInitializer implements CommandLineRunner {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        createDefaultStudentIfNotExists();
    }
    
    private void createDefaultStudentIfNotExists() {
        String defaultStudentId = "STU0000000001";
        String defaultPassword = "student123";
        
        // Check if student already exists
        if (studentRepository.findByStudentId(defaultStudentId).isEmpty()) {
            System.out.println("=================================");
            System.out.println("Creating default student account...");
            System.out.println("=================================");
            
            Student student = new Student();
            student.setStudentId(defaultStudentId);
            student.setName("John Doe");
            student.setEmail("john.doe@university.edu");
            
            // Use PasswordEncoder to hash password - GUARANTEED to work!
            student.setPassword(passwordEncoder.encode(defaultPassword));
            
            student.setRole(Role.STUDENT);
            student.setMajor("Computer Science");
            student.setPhoneNumber("555-0123");
            student.setYear("Junior");
            student.setGpa(3.75);
            student.setCredits(90);
            student.setTotalCredits(120);
            student.setAttendance(95);
            
            studentRepository.save(student);
            
            System.out.println("✅ Default student account created!");
            System.out.println("   Student ID: " + defaultStudentId);
            System.out.println("   Password: " + defaultPassword);
            System.out.println("   Name: John Doe");
            System.out.println("   Email: john.doe@university.edu");
            System.out.println("=================================");
        } else {
            System.out.println("ℹ️  Student account already exists - skipping creation");
        }
    }
}
