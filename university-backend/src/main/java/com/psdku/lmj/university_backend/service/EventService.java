/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.psdku.lmj.university_backend.service;

import com.psdku.lmj.university_backend.model.Event;
import com.psdku.lmj.university_backend.repository.EventRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *
 * @author ACER
 */
@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;
    
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByDateAfterOrderByDateAsc(LocalDate.now());
    }
    
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }
    
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }
    
    public Event registerForEvent(Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            if (event.getRegistrations() < event.getCapacity()) {
                event.setRegistrations(event.getRegistrations() + 1);
                return eventRepository.save(event);
            }
        }
        return null;
    }
}
