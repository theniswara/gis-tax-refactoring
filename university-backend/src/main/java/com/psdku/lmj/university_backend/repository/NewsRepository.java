/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * News Repository - Updated for normalized Category relationship
 */
package com.psdku.lmj.university_backend.repository;

import com.psdku.lmj.university_backend.model.Category;
import com.psdku.lmj.university_backend.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * News Repository
 * 
 * Updated to work with normalized Category relationship
 */

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    
    /**
     * Find news by Category object
     */
    List<News> findByCategory(Category category);
    
    /**
     * Find news by category ID
     */
    List<News> findByCategoryId(Long categoryId);
    
    /**
     * Find news by category name (using join)
     */
    @Query("SELECT n FROM News n WHERE n.category.name = :categoryName")
    List<News> findByCategoryName(@Param("categoryName") String categoryName);
    
    /**
     * Find news by category slug (using join)
     */
    @Query("SELECT n FROM News n WHERE n.category.slug = :slug")
    List<News> findByCategorySlug(@Param("slug") String slug);
    
    /**
     * Get all news ordered by date descending
     */
    List<News> findAllByOrderByDateDesc();
    
    /**
     * Get news by category ordered by date
     */
    List<News> findByCategoryOrderByDateDesc(Category category);
    
    /**
     * Get news by category ID ordered by date
     */
    @Query("SELECT n FROM News n WHERE n.category.id = :categoryId ORDER BY n.date DESC")
    List<News> findByCategoryIdOrderByDateDesc(@Param("categoryId") Long categoryId);
    
    /**
     * Get recent news (limit)
     */
    @Query("SELECT n FROM News n ORDER BY n.date DESC LIMIT :limit")
    List<News> findRecentNews(@Param("limit") int limit);
    
    /**
     * Get recent news by category
     */
    @Query("SELECT n FROM News n WHERE n.category.id = :categoryId ORDER BY n.date DESC LIMIT :limit")
    List<News> findRecentNewsByCategory(@Param("categoryId") Long categoryId, @Param("limit") int limit);
}
