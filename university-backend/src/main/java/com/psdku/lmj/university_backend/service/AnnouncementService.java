/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * Announcement Service
 */
package com.psdku.lmj.university_backend.service;

import com.psdku.lmj.university_backend.model.Announcement;
import com.psdku.lmj.university_backend.model.Student;
import com.psdku.lmj.university_backend.repository.AnnouncementRepository;
import com.psdku.lmj.university_backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Announcement Service
 * 
 * Business logic for managing announcements
 */
@Service
public class AnnouncementService {
    
    @Autowired
    private AnnouncementRepository announcementRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    /**
     * Get all announcements for a student
     */
    public List<Announcement> getStudentAnnouncements(Long studentId) {
        return announcementRepository.findByStudentId(studentId);
    }
    
    /**
     * Get recent announcements for a student (default: 5 most recent)
     */
    public List<Announcement> getRecentAnnouncements(Long studentId) {
        return announcementRepository.findRecentByStudentId(studentId, 5);
    }
    
    /**
     * Get recent announcements with custom limit
     */
    public List<Announcement> getRecentAnnouncements(Long studentId, int limit) {
        return announcementRepository.findRecentByStudentId(studentId, limit);
    }
    
    /**
     * Get important announcements for a student
     */
    public List<Announcement> getImportantAnnouncements(Long studentId) {
        return announcementRepository.findImportantByStudentId(studentId);
    }
    
    /**
     * Get specific announcement by ID
     */
    public Optional<Announcement> getAnnouncementById(Long id) {
        return announcementRepository.findById(id);
    }
    
    /**
     * Create new announcement for a student
     */
    public Announcement createAnnouncement(Long studentId, Announcement announcement) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        
        if (studentOptional.isEmpty()) {
            throw new RuntimeException("Student not found with id: " + studentId);
        }
        
        announcement.setStudent(studentOptional.get());
        return announcementRepository.save(announcement);
    }
    
    /**
     * Update announcement
     */
    public Announcement updateAnnouncement(Long id, Announcement announcementDetails) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + id));
        
        if (announcementDetails.getTitle() != null) {
            announcement.setTitle(announcementDetails.getTitle());
        }
        if (announcementDetails.getDate() != null) {
            announcement.setDate(announcementDetails.getDate());
        }
        if (announcementDetails.getImportant() != null) {
            announcement.setImportant(announcementDetails.getImportant());
        }
        
        return announcementRepository.save(announcement);
    }
    
    /**
     * Delete announcement
     */
    public void deleteAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + id));
        
        announcementRepository.delete(announcement);
    }
    
    /**
     * Get all announcements (admin only)
     */
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }
}
