/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * Announcement Controller
 */
package com.psdku.lmj.university_backend.controller;

import com.psdku.lmj.university_backend.model.Announcement;
import com.psdku.lmj.university_backend.service.AnnouncementService;
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
 * Announcement Controller - REST endpoints for announcement operations
 * 
 * Endpoints:
 * - GET /announcements/student/{studentId} - Get all announcements for a student
 * - GET /announcements/student/{studentId}/recent - Get recent announcements
 * - GET /announcements/student/{studentId}/important - Get important announcements
 * - GET /announcements/{id} - Get specific announcement
 * - POST /announcements/student/{studentId} - Create new announcement
 * - PUT /announcements/{id} - Update announcement
 * - DELETE /announcements/{id} - Delete announcement
 * - GET /announcements - Get all announcements (admin only)
 */
@RestController
@RequestMapping("/announcements")
public class AnnouncementController {
    
    @Autowired
    private AnnouncementService announcementService;
    
    /**
     * Get all announcements for a specific student
     * Accessible by the student themselves or admin
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Announcement>> getStudentAnnouncements(@PathVariable Long studentId) {
        List<Announcement> announcements = announcementService.getStudentAnnouncements(studentId);
        return ResponseEntity.ok(announcements);
    }
    
    /**
     * Get recent announcements for a student (default: 5 most recent)
     */
    @GetMapping("/student/{studentId}/recent")
    public ResponseEntity<List<Announcement>> getRecentAnnouncements(
            @PathVariable Long studentId,
            @RequestParam(required = false, defaultValue = "5") int limit) {
        List<Announcement> announcements = announcementService.getRecentAnnouncements(studentId, limit);
        return ResponseEntity.ok(announcements);
    }
    
    /**
     * Get important announcements for a student
     */
    @GetMapping("/student/{studentId}/important")
    public ResponseEntity<List<Announcement>> getImportantAnnouncements(@PathVariable Long studentId) {
        List<Announcement> announcements = announcementService.getImportantAnnouncements(studentId);
        return ResponseEntity.ok(announcements);
    }
    
    /**
     * Get specific announcement by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAnnouncementById(@PathVariable Long id) {
        return announcementService.getAnnouncementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create new announcement for a student
     * Accessible by admin or the student themselves
     */
    @PostMapping("/student/{studentId}")
    public ResponseEntity<?> createAnnouncement(
            @PathVariable Long studentId,
            @Valid @RequestBody Announcement announcement) {
        try {
            Announcement newAnnouncement = announcementService.createAnnouncement(studentId, announcement);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAnnouncement);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Update announcement information
     * Admins can update any announcement, students can update their own
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnnouncement(
            @PathVariable Long id,
            @Valid @RequestBody Announcement announcement) {
        try {
            Announcement updatedAnnouncement = announcementService.updateAnnouncement(id, announcement);
            return ResponseEntity.ok(updatedAnnouncement);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Delete announcement
     * Admins can delete any announcement, students can delete their own
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long id) {
        try {
            announcementService.deleteAnnouncement(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Announcement deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get all announcements (admin only)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        List<Announcement> announcements = announcementService.getAllAnnouncements();
        return ResponseEntity.ok(announcements);
    }
}
