/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.psdku.lmj.university_backend.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Security Headers Filter
 * 
 * Adds security headers to all HTTP responses:
 * - Content-Security-Policy (CSP)
 * - X-Content-Type-Options
 * - X-Frame-Options
 * - Strict-Transport-Security (HSTS)
 * - Referrer-Policy
 */
@Component
public class SecurityHeadersFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // ✅ Content Security Policy - Prevents XSS attacks
        httpResponse.setHeader("Content-Security-Policy",
            "default-src 'self'; " +
            "script-src 'self' 'unsafe-inline'; " +
            "style-src 'self' 'unsafe-inline'; " +
            "img-src 'self' data: https:; " +
            "font-src 'self' data:; " +
            "connect-src 'self'; " +
            "frame-ancestors 'none';"
        );
        
        // ✅ Prevent MIME type sniffing
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");
        
        // ✅ Prevent clickjacking
        httpResponse.setHeader("X-Frame-Options", "DENY");
        
        // ✅ HSTS - Force HTTPS (uncomment for production with HTTPS)
        // httpResponse.setHeader("Strict-Transport-Security", 
        //     "max-age=31536000; includeSubDomains; preload");
        
        // ✅ Referrer Policy - Control referer information
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        
        // ✅ Permissions Policy (formerly Feature-Policy)
        httpResponse.setHeader("Permissions-Policy",
            "geolocation=(), microphone=(), camera=()"
        );
        
        chain.doFilter(request, response);
    }
}
