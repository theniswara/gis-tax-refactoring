/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * Category Service
 */
package com.psdku.lmj.university_backend.service;

import com.psdku.lmj.university_backend.model.Category;
import com.psdku.lmj.university_backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Category Service
 * 
 * Business logic for managing categories
 */
@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    /**
     * Get all categories
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAllOrdered();
    }
    
    /**
     * Get all active categories
     */
    public List<Category> getActiveCategories() {
        return categoryRepository.findAllActive();
    }
    
    /**
     * Get category by ID
     */
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    
    /**
     * Get category by name
     */
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }
    
    /**
     * Get category by slug
     */
    public Optional<Category> getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }
    
    /**
     * Create new category
     */
    public Category createCategory(Category category) {
        // Check if name already exists
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category with name '" + category.getName() + "' already exists");
        }
        
        // Auto-generate slug if not provided
        if (category.getSlug() == null || category.getSlug().isEmpty()) {
            category.setSlug(generateSlug(category.getName()));
        }
        
        // Check if slug already exists
        if (categoryRepository.existsBySlug(category.getSlug())) {
            throw new RuntimeException("Category with slug '" + category.getSlug() + "' already exists");
        }
        
        // Set active to true if not specified
        if (category.getActive() == null) {
            category.setActive(true);
        }
        
        return categoryRepository.save(category);
    }
    
    /**
     * Update category
     */
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        
        // Update name if provided and different
        if (categoryDetails.getName() != null && !categoryDetails.getName().equals(category.getName())) {
            // Check if new name already exists
            if (categoryRepository.existsByName(categoryDetails.getName())) {
                throw new RuntimeException("Category with name '" + categoryDetails.getName() + "' already exists");
            }
            category.setName(categoryDetails.getName());
        }
        
        if (categoryDetails.getDescription() != null) {
            category.setDescription(categoryDetails.getDescription());
        }
        
        // Update slug if provided and different
        if (categoryDetails.getSlug() != null && !categoryDetails.getSlug().equals(category.getSlug())) {
            // Check if new slug already exists
            if (categoryRepository.existsBySlug(categoryDetails.getSlug())) {
                throw new RuntimeException("Category with slug '" + categoryDetails.getSlug() + "' already exists");
            }
            category.setSlug(categoryDetails.getSlug());
        }
        
        if (categoryDetails.getColor() != null) {
            category.setColor(categoryDetails.getColor());
        }
        
        if (categoryDetails.getActive() != null) {
            category.setActive(categoryDetails.getActive());
        }
        
        if (categoryDetails.getDisplayOrder() != null) {
            category.setDisplayOrder(categoryDetails.getDisplayOrder());
        }
        
        return categoryRepository.save(category);
    }
    
    /**
     * Delete category
     */
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        
        categoryRepository.delete(category);
    }
    
    /**
     * Deactivate category (soft delete)
     */
    public Category deactivateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        
        category.setActive(false);
        return categoryRepository.save(category);
    }
    
    /**
     * Activate category
     */
    public Category activateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        
        category.setActive(true);
        return categoryRepository.save(category);
    }
    
    /**
     * Generate URL-friendly slug from name
     */
    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // Remove special characters
                .replaceAll("\\s+", "-")          // Replace spaces with hyphens
                .replaceAll("-+", "-")            // Replace multiple hyphens with single
                .replaceAll("^-|-$", "");         // Remove leading/trailing hyphens
    }
}
