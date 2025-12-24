/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * News Controller - Updated for normalized Category relationship
 */
package com.psdku.lmj.university_backend.controller;

import com.psdku.lmj.university_backend.model.News;
import com.psdku.lmj.university_backend.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * News Controller
 * 
 * REST endpoints for news operations with normalized categories
 * 
 * Public endpoints:
 * - GET /news - Get all news
 * - GET /news/{id} - Get specific news
 * - GET /news/category/{slug} - Get news by category slug (SEO-friendly)
 * - GET /news/category/id/{categoryId} - Get news by category ID
 * - GET /news/category/name/{name} - Get news by category name
 * - GET /news/recent?limit=10 - Get recent news
 * - PUT /news/{id}/views - Increment view count
 * - PUT /news/{id}/likes - Increment like count
 * 
 * Admin endpoints:
 * - POST /news - Create news
 * - PUT /news/{id} - Update news
 * - DELETE /news/{id} - Delete news
 */
@RestController
@RequestMapping("/news")
public class NewsController {
    
    @Autowired
    private NewsService newsService;
    
    /**
     * Get all news (ordered by date, newest first)
     * Public access
     */
    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        return ResponseEntity.ok(newsService.getAllNews());
    }
    
    /**
     * Get specific news by ID
     * Public access
     */
    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        Optional<News> news = newsService.getNewsById(id);
        return news.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get news by category slug (SEO-friendly)
     * Example: /news/category/academic
     * Public access
     */
    @GetMapping("/category/{slug}")
    public ResponseEntity<List<News>> getNewsByCategorySlug(@PathVariable String slug) {
        List<News> news = newsService.getNewsByCategorySlug(slug);
        return ResponseEntity.ok(news);
    }
    
    /**
     * Get news by category ID
     * Example: /news/category/id/1
     * Public access
     */
    @GetMapping("/category/id/{categoryId}")
    public ResponseEntity<List<News>> getNewsByCategoryId(@PathVariable Long categoryId) {
        List<News> news = newsService.getNewsByCategoryId(categoryId);
        return ResponseEntity.ok(news);
    }
    
    /**
     * Get news by category name
     * Example: /news/category/name/Academic
     * Public access
     */
    @GetMapping("/category/name/{name}")
    public ResponseEntity<List<News>> getNewsByCategoryName(@PathVariable String name) {
        List<News> news = newsService.getNewsByCategoryName(name);
        return ResponseEntity.ok(news);
    }
    
    /**
     * Get recent news (limited number)
     * Example: /news/recent?limit=10
     * Public access
     */
    @GetMapping("/recent")
    public ResponseEntity<List<News>> getRecentNews(
            @RequestParam(required = false, defaultValue = "10") int limit) {
        List<News> news = newsService.getRecentNews(limit);
        return ResponseEntity.ok(news);
    }
    
    /**
     * Get recent news by category ID
     * Example: /news/category/1/recent?limit=5
     * Public access
     */
    @GetMapping("/category/{categoryId}/recent")
    public ResponseEntity<List<News>> getRecentNewsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(required = false, defaultValue = "5") int limit) {
        List<News> news = newsService.getRecentNewsByCategory(categoryId, limit);
        return ResponseEntity.ok(news);
    }
    
    /**
     * Create new news
     * Admin only
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createNews(
            @RequestBody News news,
            @RequestParam(required = false) Long categoryId) {
        try {
            News createdNews = categoryId != null 
                ? newsService.createNews(news, categoryId)
                : newsService.saveNews(news);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Update news
     * Admin only
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateNews(
            @PathVariable Long id,
            @RequestBody News news) {
        try {
            News updatedNews = newsService.updateNews(id, news);
            return ResponseEntity.ok(updatedNews);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Delete news
     * Admin only
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        try {
            newsService.deleteNews(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "News deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Increment view count
     * Public access
     */
    @PutMapping("/{id}/views")
    public ResponseEntity<News> incrementViews(@PathVariable Long id) {
        News news = newsService.incrementViews(id);
        if (news != null) {
            return ResponseEntity.ok(news);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Increment like count
     * Public access
     */
    @PutMapping("/{id}/likes")
    public ResponseEntity<News> incrementLikes(@PathVariable Long id) {
        News news = newsService.incrementLikes(id);
        if (news != null) {
            return ResponseEntity.ok(news);
        }
        return ResponseEntity.notFound().build();
    }
}
