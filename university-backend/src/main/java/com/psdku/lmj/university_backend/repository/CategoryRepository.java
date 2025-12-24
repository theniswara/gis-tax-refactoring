/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * Category Repository
 */
package com.psdku.lmj.university_backend.repository;

import com.psdku.lmj.university_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Category Repository
 * 
 * Provides methods for querying categories
 */

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * Find category by name
     */
    Optional<Category> findByName(String name);
    
    /**
     * Find category by slug (URL-friendly name)
     */
    Optional<Category> findBySlug(String slug);
    
    /**
     * Find all active categories
     */
    @Query("SELECT c FROM Category c WHERE c.active = true ORDER BY c.displayOrder ASC, c.name ASC")
    List<Category> findAllActive();
    
    /**
     * Find categories ordered by display order
     */
    @Query("SELECT c FROM Category c ORDER BY c.displayOrder ASC, c.name ASC")
    List<Category> findAllOrdered();
    
    /**
     * Check if category name exists
     */
    boolean existsByName(String name);
    
    /**
     * Check if category slug exists
     */
    boolean existsBySlug(String slug);
}
