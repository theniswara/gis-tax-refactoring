/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.psdku.lmj.university_backend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Account Lockout Service - Prevents brute force attacks
 * 
 * Security Features:
 * - Tracks failed login attempts
 * - Locks account after max attempts
 * - Automatic unlock after timeout
 * - Reset on successful login
 */
@Service
public class AccountLockoutService {
    
    @Value("${security.account.max-failed-attempts:5}")
    private int maxFailedAttempts;
    
    @Value("${security.account.lockout-duration-minutes:15}")
    private int lockoutDurationMinutes;
    
    // Store login attempts per student ID
    private final Map<String, LoginAttempt> loginAttempts = new ConcurrentHashMap<>();
    
    /**
     * Record a failed login attempt
     */
    public void recordFailedAttempt(String studentId) {
        LoginAttempt attempt = loginAttempts.computeIfAbsent(
            studentId,
            k -> new LoginAttempt()
        );
        
        attempt.incrementAttempts();
        attempt.setLastAttempt(LocalDateTime.now());
        
        // Lock account if max attempts reached
        if (attempt.getAttempts() >= maxFailedAttempts) {
            attempt.setLockedUntil(LocalDateTime.now().plusMinutes(lockoutDurationMinutes));
        }
    }
    
    /**
     * Reset login attempts on successful login
     */
    public void resetFailedAttempts(String studentId) {
        loginAttempts.remove(studentId);
    }
    
    /**
     * Reset attempts (alias for resetFailedAttempts)
     */
    public void resetAttempts(String studentId) {
        resetFailedAttempts(studentId);
    }
    
    /**
     * Check if account is locked (short name)
     */
    public boolean isLocked(String studentId) {
        return isAccountLocked(studentId);
    }
    
    /**
     * Check if account is locked
     */
    public boolean isAccountLocked(String studentId) {
        LoginAttempt attempt = loginAttempts.get(studentId);
        
        if (attempt == null) {
            return false;
        }
        
        // Check if lockout has expired
        if (attempt.getLockedUntil() != null) {
            if (LocalDateTime.now().isAfter(attempt.getLockedUntil())) {
                // Lockout expired, reset attempts
                loginAttempts.remove(studentId);
                return false;
            }
            return true;
        }
        
        return false;
    }
    
    /**
     * Get remaining time in minutes until account unlocks
     */
    public int getRemainingLockoutTime(String studentId) {
        LoginAttempt attempt = loginAttempts.get(studentId);
        if (attempt == null || attempt.getLockedUntil() == null) {
            return 0;
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(attempt.getLockedUntil())) {
            return 0;
        }
        
        // Calculate remaining minutes
        long seconds = java.time.Duration.between(now, attempt.getLockedUntil()).getSeconds();
        return (int) Math.ceil(seconds / 60.0);
    }
    
    /**
     * Get remaining attempts before lockout
     */
    public int getRemainingAttempts(String studentId) {
        LoginAttempt attempt = loginAttempts.get(studentId);
        if (attempt == null) {
            return maxFailedAttempts;
        }
        return Math.max(0, maxFailedAttempts - attempt.getAttempts());
    }
    
    /**
     * Get lockout end time
     */
    public LocalDateTime getLockoutEndTime(String studentId) {
        LoginAttempt attempt = loginAttempts.get(studentId);
        return attempt != null ? attempt.getLockedUntil() : null;
    }
    
    /**
     * Inner class to track login attempts
     */
    private static class LoginAttempt {
        private int attempts = 0;
        private LocalDateTime lastAttempt;
        private LocalDateTime lockedUntil;
        
        public void incrementAttempts() {
            this.attempts++;
        }
        
        public int getAttempts() {
            return attempts;
        }
        
        public LocalDateTime getLastAttempt() {
            return lastAttempt;
        }
        
        public void setLastAttempt(LocalDateTime lastAttempt) {
            this.lastAttempt = lastAttempt;
        }
        
        public LocalDateTime getLockedUntil() {
            return lockedUntil;
        }
        
        public void setLockedUntil(LocalDateTime lockedUntil) {
            this.lockedUntil = lockedUntil;
        }
    }
}