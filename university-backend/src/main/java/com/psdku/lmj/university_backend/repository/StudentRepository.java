/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.psdku.lmj.university_backend.repository;

import com.psdku.lmj.university_backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Student Repository with secure queries
 * 
 * Security Features:
 * - Parameterized queries (prevents SQL injection)
 * - No plain password queries
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * Find student by student ID
     * ✅ Uses parameterized query
     */
    @Query("SELECT s FROM Student s WHERE s.studentId = :studentId")
    Optional<Student> findByStudentId(@Param("studentId") String studentId);
    
    /**
     * Find student by email
     * ✅ Uses parameterized query
     */
    @Query("SELECT s FROM Student s WHERE s.email = :email")
    Optional<Student> findByEmail(@Param("email") String email);
    
    /**
     * Check if student ID exists
     */
    boolean existsByStudentId(String studentId);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
}
