/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * Category Entity
 * Normalized category table for News and other content
 */
package com.psdku.lmj.university_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Category Entity
 * 
 * Used for categorizing News, Announcements, and other content
 * Examples: Academic, Sports, Events, Research, Campus Life
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    
    @Column(length = 255)
    private String description;
    
    @Column(length = 50)
    private String slug; // URL-friendly version: "campus-life" instead of "Campus Life"
    
    @Column(length = 7)
    private String color; // Hex color for UI display, e.g., "#FF5733"
    
    @Column(nullable = false)
    private Boolean active = true; // Can disable categories without deleting
    
    @Column(name = "display_order")
    private Integer displayOrder; // For ordering categories in UI
    
    // One category can have many news articles
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore // Prevent circular reference in JSON serialization
    private List<News> newsList;
    
    // Constructor for easy creation
    public Category(String name, String description, String slug, String color) {
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.color = color;
        this.active = true;
    }
}
