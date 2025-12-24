/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.psdku.lmj.university_backend.security;

import com.psdku.lmj.university_backend.model.Admin;
import com.psdku.lmj.university_backend.model.Student;
import com.psdku.lmj.university_backend.repository.AdminRepository;
import com.psdku.lmj.university_backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * Custom UserDetailsService implementation for Spring Security
 * 
 * Supports both Student and Admin authentication with role-based access
 * - Students: ROLE_STUDENT
 * - Admins: ROLE_ADMIN
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to find as student first (by studentId)
        Optional<Student> studentOpt = studentRepository.findByStudentId(username);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            return User.builder()
                    .username(student.getStudentId())
                    .password(student.getPassword())
                    .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority(student.getRole().getAuthority())
                    ))
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();
        }
        
        // Try to find as admin (by username)
        Optional<Admin> adminOpt = adminRepository.findActiveByUsername(username);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return User.builder()
                    .username(admin.getUsername())
                    .password(admin.getPassword())
                    .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority(admin.getRole().getAuthority())
                    ))
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(!admin.isActive())
                    .build();
        }
        
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
