/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.psdku.lmj.university_backend.controller;

import com.psdku.lmj.university_backend.dto.LoginRequest;
import com.psdku.lmj.university_backend.dto.LoginResponse;
import com.psdku.lmj.university_backend.security.RateLimitingService;
import com.psdku.lmj.university_backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Secure Authentication Controller
 * 
 * Security Features:
 * - Rate limiting
 * - Input validation
 * - JWT token generation
 * - Account lockout integration
 * - Secure error handling
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private RateLimitingService rateLimitingService;
    
    /**
     * Login endpoint with rate limiting and validation
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest loginRequest,
            BindingResult bindingResult,
            HttpServletRequest request) {
        
        // ✅ Validate input
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }
        
        // ✅ Rate limiting by IP address
        String clientIp = getClientIp(request);
        if (!rateLimitingService.tryConsume(clientIp)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new LoginResponse(false, 
                        "Too many login attempts. Please try again later."));
        }
        
        // ✅ Attempt login
        LoginResponse response = authService.login(loginRequest);
        
        if (response.isSuccess()) {
            // Reset rate limit on successful login
            rateLimitingService.reset(clientIp);
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    /**
     * Token refresh endpoint
     */
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse(false, "Refresh token is required"));
        }
        
        LoginResponse response = authService.refreshToken(refreshToken);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    /**
     * Logout endpoint (client-side token removal)
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // With JWT, logout is handled client-side by removing the token
        // Optionally, you can implement token blacklisting here
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
    
    /**
     * Get client IP address from request
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
