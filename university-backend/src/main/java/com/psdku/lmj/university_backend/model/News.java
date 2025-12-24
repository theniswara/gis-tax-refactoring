/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * News Entity - Updated with normalized Category relationship
 */
package com.psdku.lmj.university_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * News Entity
 * 
 * Represents news articles with normalized category relationship
 */

@Entity
@Table(name = "news")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    private LocalDate date;
    
    // ✅ NORMALIZED: Many news articles can have one category
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;
    
    private String author;
    
    @Column(length = 500)
    private String excerpt;
    
    private String image;
    
    @Column(length = 5000)
    private String content;
    
    private Integer views;
    private Integer likes;
    
    /**
     * Helper method to get category name (for backward compatibility)
     */
    @Transient
    public String getCategoryName() {
        return category != null ? category.getName() : null;
    }
}
