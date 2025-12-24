/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.psdku.lmj.university_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Student Entity with Security Features
 * 
 * Security Enhancements:
 * - Password excluded from JSON responses
 * - Input validation
 * - Size constraints
 * - Email format validation
 */
@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Student ID is required")
    @Pattern(regexp = "^STU\\d{10}$", message = "Invalid student ID format")
    @Column(unique = true, nullable = false, length = 20)
    private String studentId;
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.STUDENT;  // Default role for students
    
    // ✅ Password security:
    // - Never returned in JSON responses (JsonIgnore)
    // - Only accepted during input (JsonProperty WRITE_ONLY)
    // - Stored as BCrypt hash (60 characters)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(nullable = false, length = 60) // BCrypt hash length
    private String password;
    
    @Size(max = 100)
    private String major;
    
    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Size(max = 20)
    private String year;
    
    @DecimalMin(value = "0.0", message = "GPA must be at least 0.0")
    @DecimalMax(value = "4.0", message = "GPA must not exceed 4.0")
    private Double gpa;
    
    @Min(value = 0, message = "Credits must be non-negative")
    private Integer credits;
    
    @Min(value = 0, message = "Total credits must be non-negative")
    private Integer totalCredits;
    
    @Min(value = 0, message = "Attendance must be non-negative")
    @Max(value = 100, message = "Attendance cannot exceed 100")
    private Integer attendance;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnore // Prevent circular references
    private List<Course> currentCourses;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnore // Prevent circular references
    private List<Assignment> upcomingAssignments;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnore // Prevent circular references
    private List<Announcement> announcements;
}

