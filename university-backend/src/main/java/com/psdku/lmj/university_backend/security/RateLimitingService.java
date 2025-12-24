/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.psdku.lmj.university_backend.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate Limiting Service - Prevents brute force and DoS attacks
 * 
 * Uses Token Bucket algorithm:
 * - Each IP/user gets a bucket with limited tokens
 * - Each request consumes a token
 * - Tokens refill over time
 * - Requests rejected when bucket is empty
 */
@Service
public class RateLimitingService {
    
    @Value("${rate.limit.login.capacity:5}")
    private int loginCapacity;
    
    @Value("${rate.limit.login.refill-tokens:5}")
    private int refillTokens;
    
    @Value("${rate.limit.login.refill-period-minutes:1}")
    private int refillPeriodMinutes;
    
    // Cache for storing buckets per IP address
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    
    /**
     * Resolve bucket for given key (IP address or user ID)
     */
    public Bucket resolveBucket(String key) {
        return cache.computeIfAbsent(key, this::createNewBucket);
    }
    
    /**
     * Create a new bucket with configured limits
     */
    private Bucket createNewBucket(String key) {
        // Define rate limit: refillTokens per refillPeriod
        Bandwidth limit = Bandwidth.classic(
            loginCapacity,
            Refill.intervally(refillTokens, Duration.ofMinutes(refillPeriodMinutes))
        );
        
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
    
    /**
     * Try to consume a token from the bucket
     * Returns true if token available, false if rate limit exceeded
     */
    public boolean tryConsume(String key) {
        Bucket bucket = resolveBucket(key);
        return bucket.tryConsume(1);
    }
    
    /**
     * Get available tokens for a key
     */
    public long getAvailableTokens(String key) {
        Bucket bucket = resolveBucket(key);
        return bucket.getAvailableTokens();
    }
    
    /**
     * Clear rate limit for a key (e.g., after successful login)
     */
    public void reset(String key) {
        cache.remove(key);
    }
}
