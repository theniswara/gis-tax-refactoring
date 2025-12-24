/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.psdku.lmj.university_backend.repository;

import com.psdku.lmj.university_backend.model.Course;
import com.psdku.lmj.university_backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Course Repository - Database operations for Course entity
 * 
 * Provides methods to query courses by student, semester, etc.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    /**
     * Find all courses for a specific student
     */
    @Query("SELECT c FROM Course c WHERE c.student.id = :studentId")
    List<Course> findByStudentId(@Param("studentId") Long studentId);
    
    /**
     * Find all courses for a specific student by student object
     */
    List<Course> findByStudent(Student student);
    
    /**
     * Find courses by student ID and semester (if you want to add semester later)
     */
    @Query("SELECT c FROM Course c WHERE c.student.id = :studentId ORDER BY c.name")
    List<Course> findByStudentIdOrderByName(@Param("studentId") Long studentId);
    
    /**
     * Count courses for a student
     */
    @Query("SELECT COUNT(c) FROM Course c WHERE c.student.id = :studentId")
    Long countByStudentId(@Param("studentId") Long studentId);
    
    /**
     * Find courses by course code
     */
    List<Course> findByCode(String code);
    
    /**
     * Delete all courses for a student
     */
    void deleteByStudent(Student student);
}
