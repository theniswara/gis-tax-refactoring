/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * Assignment Controller
 */
package com.psdku.lmj.university_backend.controller;

import com.psdku.lmj.university_backend.model.Assignment;
import com.psdku.lmj.university_backend.service.AssignmentService;
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
 * Assignment Controller - REST endpoints for assignment operations
 * 
 * Endpoints:
 * - GET /assignments/student/{studentId} - Get all assignments for a student
 * - GET /assignments/student/{studentId}/upcoming - Get upcoming assignments
 * - GET /assignments/student/{studentId}/status/{status} - Get assignments by status
 * - GET /assignments/{id} - Get specific assignment
 * - POST /assignments/student/{studentId} - Create new assignment
 * - PUT /assignments/{id} - Update assignment
 * - DELETE /assignments/{id} - Delete assignment
 * - GET /assignments - Get all assignments (admin only)
 */
@RestController
@RequestMapping("/assignments")
public class AssignmentController {
    
    @Autowired
    private AssignmentService assignmentService;
    
    /**
     * Get all assignments for a specific student
     * Accessible by the student themselves or admin
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Assignment>> getStudentAssignments(@PathVariable Long studentId) {
        List<Assignment> assignments = assignmentService.getStudentAssignments(studentId);
        return ResponseEntity.ok(assignments);
    }
    
    /**
     * Get upcoming assignments for a student (due date >= today)
     */
    @GetMapping("/student/{studentId}/upcoming")
    public ResponseEntity<List<Assignment>> getUpcomingAssignments(@PathVariable Long studentId) {
        List<Assignment> assignments = assignmentService.getUpcomingAssignments(studentId);
        return ResponseEntity.ok(assignments);
    }
    
    /**
     * Get assignments by status for a student
     * Status values: "Pending", "In Progress", "Completed", "Overdue"
     */
    @GetMapping("/student/{studentId}/status/{status}")
    public ResponseEntity<List<Assignment>> getAssignmentsByStatus(
            @PathVariable Long studentId,
            @PathVariable String status) {
        List<Assignment> assignments = assignmentService.getAssignmentsByStatus(studentId, status);
        return ResponseEntity.ok(assignments);
    }
    
    /**
     * Get specific assignment by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAssignmentById(@PathVariable Long id) {
        return assignmentService.getAssignmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create new assignment for a student
     * Accessible by admin or the student themselves
     */
    @PostMapping("/student/{studentId}")
    public ResponseEntity<?> createAssignment(
            @PathVariable Long studentId,
            @Valid @RequestBody Assignment assignment) {
        try {
            Assignment newAssignment = assignmentService.createAssignment(studentId, assignment);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAssignment);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Update assignment information
     * Admins can update any assignment, students can update their own
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssignment(
            @PathVariable Long id,
            @Valid @RequestBody Assignment assignment) {
        try {
            Assignment updatedAssignment = assignmentService.updateAssignment(id, assignment);
            return ResponseEntity.ok(updatedAssignment);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Delete assignment
     * Admins can delete any assignment, students can delete their own
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable Long id) {
        try {
            assignmentService.deleteAssignment(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Assignment deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get all assignments (admin only)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        List<Assignment> assignments = assignmentService.getAllAssignments();
        return ResponseEntity.ok(assignments);
    }
}
