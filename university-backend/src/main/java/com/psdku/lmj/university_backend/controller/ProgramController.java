/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.psdku.lmj.university_backend.controller;

import com.psdku.lmj.university_backend.dto.ApiResponse;
import com.psdku.lmj.university_backend.model.Program;
import com.psdku.lmj.university_backend.service.ProgramService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author LENOVO
 */
@RestController
@RequestMapping("/programs")
public class ProgramController {
    
    @Autowired
    private ProgramService programService;
    
    @GetMapping
    public ResponseEntity<List<Program>> getAllPrograms() {
        return ResponseEntity.ok(programService.getAllPrograms());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Program> getProgramById(@PathVariable Long id) {
        Optional<Program> program = programService.getProgramById(id);
        return program.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Program>> searchPrograms(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String degree,
            @RequestParam(required = false) String field) {
        return ResponseEntity.ok(programService.searchPrograms(query, degree, field));
    }
    
    @PostMapping
    public ResponseEntity<Program> createProgram(@RequestBody Program program) {
        return ResponseEntity.ok(programService.saveProgram(program));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Program> updateProgram(@PathVariable Long id, @RequestBody Program program) {
        program.setId(id);
        return ResponseEntity.ok(programService.saveProgram(program));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProgram(@PathVariable Long id) {
        programService.deleteProgram(id);
        return ResponseEntity.ok(new ApiResponse(true, "Program deleted successfully", null));
    }
}
