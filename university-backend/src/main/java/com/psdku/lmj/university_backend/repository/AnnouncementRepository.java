/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * Announcement Repository with query methods
 */
package com.psdku.lmj.university_backend.repository;

import com.psdku.lmj.university_backend.model.Announcement;
import com.psdku.lmj.university_backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Announcement Repository
 * 
 * Provides methods for querying announcements
 */

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    
    /**
     * Find all announcements for a specific student
     */
    List<Announcement> findByStudent(Student student);
    
    /**
     * Find all announcements for a student by student ID
     */
    @Query("SELECT a FROM Announcement a WHERE a.student.id = :studentId ORDER BY a.id DESC")
    List<Announcement> findByStudentId(@Param("studentId") Long studentId);
    
    /**
     * Find important announcements for a student
     */
    @Query("SELECT a FROM Announcement a WHERE a.student.id = :studentId AND a.important = true ORDER BY a.id DESC")
    List<Announcement> findImportantByStudentId(@Param("studentId") Long studentId);
    
    /**
     * Find recent announcements (limit results)
     */
    @Query("SELECT a FROM Announcement a WHERE a.student.id = :studentId ORDER BY a.id DESC LIMIT :limit")
    List<Announcement> findRecentByStudentId(@Param("studentId") Long studentId, @Param("limit") int limit);
}
