/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.psdku.lmj.university_backend.config;

import com.psdku.lmj.university_backend.security.CustomUserDetailsService;
import com.psdku.lmj.university_backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security Configuration
 * 
 * Security Features Implemented:
 * - JWT-based authentication
 * - BCrypt password encoding (strength 12)
 * - Stateless session management
 * - CORS configuration
 * - Public/Private endpoint segregation
 * - Security headers
 * - Role-based access control (ADMIN, STUDENT)
 * 
 * IMPORTANT NOTE:
 * - Context-path is set to "/api" in application.properties
 * - Spring Security sees URLs WITHOUT the context-path
 * - Therefore, requestMatchers should NOT include "/api" prefix
 * - Example: URL "/api/programs" is matched by requestMatchers("/programs/**")
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    /**
     * Configure HTTP security
     * 
     * URL Mapping Examples:
     * - http://localhost:8080/api/programs → matches requestMatchers("/programs/**")
     * - http://localhost:8080/api/admin/login → matches requestMatchers("/admin/login")
     * - http://localhost:8080/api/courses → matches requestMatchers("/courses/**")
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for stateless JWT authentication
            .csrf(csrf -> csrf.disable())
            
            // Configure CORS (uses CorsConfig bean)
            .cors(cors -> {})
            
            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                // ============================================
                // PUBLIC ENDPOINTS - No authentication required
                // ============================================
                
                // Authentication endpoints
                .requestMatchers("/auth/**").permitAll()           // Student login, refresh token
                .requestMatchers("/admin/login").permitAll()       // Admin login
                
                // Public content endpoints
                .requestMatchers("/programs/**").permitAll()       // Browse programs
                .requestMatchers("/news/**").permitAll()           // Browse news
                .requestMatchers("/events/**").permitAll()         // Browse events
                
                // ============================================
                // PROTECTED ENDPOINTS - Authentication required
                // ============================================
                
                // Course endpoints - Requires authentication (Student or Admin)
                .requestMatchers("/courses/**").authenticated()
                
                // Student endpoints - Requires authentication
                .requestMatchers("/students/**").authenticated()
                
                // ============================================
                // ADMIN-ONLY ENDPOINTS - Requires ADMIN role
                // ============================================
                
                // Admin management endpoints
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // ============================================
                // DEFAULT - All other requests require authentication
                // ============================================
                .anyRequest().authenticated()
            )
            
            // Stateless session management (JWT doesn't need sessions)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Add JWT filter before Spring Security's authentication filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            
            // Configure security headers
            .headers(headers -> headers
                .frameOptions(frame -> frame.deny())           // Prevent clickjacking
                .xssProtection(xss -> xss.disable())          // XSS handled by CSP in CorsConfig
                .contentTypeOptions(contentType -> contentType.disable())
            );
        
        return http.build();
    }
    
    /**
     * Password encoder using BCrypt
     * 
     * BCrypt Features:
     * - Automatically handles salt generation
     * - Resistant to brute force attacks
     * - Strength factor 12 = 2^12 iterations (4096 rounds)
     * - Higher strength = more secure but slower
     * 
     * Password hashing example:
     * Input: "admin123"
     * Output: "$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5nduLbZ4jJGWu"
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Strength factor: 12
    }
    
    /**
     * Authentication provider with custom UserDetailsService and password encoder
     * 
     * This provider:
     * - Loads user details from database via CustomUserDetailsService
     * - Verifies passwords using BCrypt encoder
     * - Returns authenticated user on success
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    /**
     * Authentication manager for processing authentication requests
     * 
     * Used by login endpoints to authenticate users
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}

