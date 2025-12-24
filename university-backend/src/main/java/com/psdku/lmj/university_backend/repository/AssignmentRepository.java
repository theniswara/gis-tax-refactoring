/*
 * Assignment Repository with query methods
 */
package com.psdku.lmj.university_backend.repository;

import com.psdku.lmj.university_backend.model.Assignment;
import com.psdku.lmj.university_backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Assignment Repository
 * 
 * Provides methods for querying assignments
 */

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    
    /**
     * Find all assignments for a specific student
     */
    List<Assignment> findByStudent(Student student);
    
    /**
     * Find all assignments for a student by student ID
     */
    @Query("SELECT a FROM Assignment a WHERE a.student.id = :studentId")
    List<Assignment> findByStudentId(@Param("studentId") Long studentId);
    
    /**
     * Find upcoming assignments (due date >= today) for a student
     * Ordered by due date ascending
     */
    @Query("SELECT a FROM Assignment a WHERE a.student.id = :studentId AND a.dueDate >= :currentDate ORDER BY a.dueDate ASC")
    List<Assignment> findUpcomingByStudentId(@Param("studentId") Long studentId, @Param("currentDate") LocalDate currentDate);
    
    /**
     * Find assignments by status for a student
     */
    @Query("SELECT a FROM Assignment a WHERE a.student.id = :studentId AND a.status = :status ORDER BY a.dueDate ASC")
    List<Assignment> findByStudentIdAndStatus(@Param("studentId") Long studentId, @Param("status") String status);
}