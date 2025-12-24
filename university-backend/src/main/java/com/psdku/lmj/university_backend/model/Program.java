/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.psdku.lmj.university_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @author LENOVO
 */
@Entity
@Table(name = "programs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Program {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String degree;
    
    private String duration;
    private Integer credits;
    private String tuition;
    
    @Column(length = 1000)
    private String description;
    
    private String image;
    private String field;
    private Double rating;
    private Integer students;
    private Integer employmentRate;
    
    @ElementCollection
    @CollectionTable(name = "program_highlights", joinColumns = @JoinColumn(name = "program_id"))
    @Column(name = "highlight")
    private List<String> highlights;
    
    @ElementCollection
    @CollectionTable(name = "program_careers", joinColumns = @JoinColumn(name = "program_id"))
    @Column(name = "career")
    private List<String> careers;
}
