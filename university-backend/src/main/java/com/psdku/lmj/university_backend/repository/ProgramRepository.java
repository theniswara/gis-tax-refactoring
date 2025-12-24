/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.psdku.lmj.university_backend.repository;

import com.psdku.lmj.university_backend.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author LENOVO
 */

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
    List<Program> findByField(String field);
    List<Program> findByDegreeContaining(String degree);
    List<Program> findByNameContainingOrDescriptionContaining(String name, String description);
}
