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

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author LENOVO
 */
@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    private LocalDate date;
    private String time;
    private String location;
    private String type;
    
    @Column(length = 1000)
    private String description;
    
    private String image;
    private Integer registrations;
    private Integer capacity;
    
    @ElementCollection
    @CollectionTable(name = "event_speakers", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "speaker")
    private List<String> speakers;
}
