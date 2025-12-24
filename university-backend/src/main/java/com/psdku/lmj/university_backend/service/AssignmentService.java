/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * Assignment Service
 */
package com.psdku.lmj.university_backend.service;

import com.psdku.lmj.university_backend.model.Assignment;
import com.psdku.lmj.university_backend.model.Student;
import com.psdku.lmj.university_backend.repository.AssignmentRepository;
import com.psdku.lmj.university_backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Assignment Service
 * 
 * Business logic for managing assignments
 */
@Service
public class AssignmentService {
    
    @Autowired
    private AssignmentRepository assignmentRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * Get all assignments for a student
     */
    public List<Assignment> getStudentAssignments(Long studentId) {
        return assignmentRepository.findByStudentId(studentId);
    }
    
    /**
     * Get upcoming assignments for a student (due date >= today)
     */
    public List<Assignment> getUpcomingAssignments(Long studentId) {
        LocalDate today = LocalDate.now();
        return assignmentRepository.findUpcomingByStudentId(studentId, today);
    }
    
    /**
     * Get assignments by status for a student
     */
    public List<Assignment> getAssignmentsByStatus(Long studentId, String status) {
        return assignmentRepository.findByStudentIdAndStatus(studentId, status);
    }
    
    /**
     * Get specific assignment by ID
     */
    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }
    
    /**
     * Create new assignment for a student
     */
    public Assignment createAssignment(Long studentId, Assignment assignment) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        
        if (studentOptional.isEmpty()) {
            throw new RuntimeException("Student not found with id: " + studentId);
        }
        
        assignment.setStudent(studentOptional.get());
        return assignmentRepository.save(assignment);
    }
    
    /**
     * Update assignment
     */
    public Assignment updateAssignment(Long id, Assignment assignmentDetails) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
        
        if (assignmentDetails.getCourse() != null) {
            assignment.setCourse(assignmentDetails.getCourse());
        }
        if (assignmentDetails.getTitle() != null) {
            assignment.setTitle(assignmentDetails.getTitle());
        }
        if (assignmentDetails.getDueDate() != null) {
            assignment.setDueDate(assignmentDetails.getDueDate());
        }
        if (assignmentDetails.getStatus() != null) {
            assignment.setStatus(assignmentDetails.getStatus());
        }
        
        return assignmentRepository.save(assignment);
    }
    
    /**
     * Delete assignment
     */
    public void deleteAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
        
        assignmentRepository.delete(assignment);
    }
    
    /**
     * Get all assignments (admin only)
     */
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }
}
