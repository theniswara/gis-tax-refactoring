/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.psdku.lmj.university_backend.controller;

import com.psdku.lmj.university_backend.model.Course;
import com.psdku.lmj.university_backend.service.CourseService;
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
 * Course Controller - REST endpoints for course operations
 * 
 * Endpoints:
 * - GET /students/{studentId}/courses - Get student's courses
 * - POST /students/{studentId}/courses - Enroll in course
 * - PUT /courses/{id} - Update course
 * - DELETE /courses/{id} - Drop course
 * - GET /courses/{id} - Get course details
 */
@RestController
@RequestMapping("/courses")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    /**
     * Get all courses for a specific student
     * Accessible by the student themselves or admin
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Course>> getStudentCourses(@PathVariable Long studentId) {
        List<Course> courses = courseService.getStudentCourses(studentId);
        return ResponseEntity.ok(courses);
    }
    
    /**
     * Get course statistics for a student
     */
    @GetMapping("/student/{studentId}/statistics")
    public ResponseEntity<CourseService.CourseStatistics> getStudentCourseStatistics(@PathVariable Long studentId) {
        CourseService.CourseStatistics stats = courseService.getStudentCourseStatistics(studentId);
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Get all courses (admin only)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
    
    /**
     * Get specific course by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Enroll student in a course
     * Accessible by admin or the student themselves
     */
    @PostMapping("/student/{studentId}")
    public ResponseEntity<?> enrollStudentInCourse(
            @PathVariable Long studentId,
            @Valid @RequestBody Course course) {
        try {
            Course newCourse = courseService.enrollStudentInCourse(studentId, course);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCourse);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Update course information
     * Admins can update any course, students can update their own
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(
            @PathVariable Long id,
            @RequestBody Course course) {
        try {
            Course updatedCourse = courseService.updateCourse(id, course);
            return ResponseEntity.ok(updatedCourse);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Drop a course
     * Students can drop their own courses, admins can drop any
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> dropCourse(@PathVariable Long id) {
        try {
            courseService.dropCourse(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Course dropped successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
