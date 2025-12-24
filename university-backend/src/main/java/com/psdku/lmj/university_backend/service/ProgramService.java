/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.psdku.lmj.university_backend.service;

import com.psdku.lmj.university_backend.model.Program;
import com.psdku.lmj.university_backend.repository.ProgramRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author LENOVO
 */
@Service
public class ProgramService {
    
    @Autowired
    private ProgramRepository programRepository;
    
    public List<Program> getAllPrograms() {
        return programRepository.findAll();
    }
    
    public Optional<Program> getProgramById(Long id) {
        return programRepository.findById(id);
    }
    
    public List<Program> searchPrograms(String query, String degree, String field) {
        if (query != null && !query.isEmpty()) {
            return programRepository.findByNameContainingOrDescriptionContaining(query, query);
        }
        if (field != null && !field.equals("all")) {
            return programRepository.findByField(field);
        }
        if (degree != null && !degree.equals("all")) {
            return programRepository.findByDegreeContaining(degree);
        }
        return programRepository.findAll();
    }
    
    public Program saveProgram(Program program) {
        return programRepository.save(program);
    }
    
    public void deleteProgram(Long id) {
        programRepository.deleteById(id);
    }
}
