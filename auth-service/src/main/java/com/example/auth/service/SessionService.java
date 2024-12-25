package com.example.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SessionService {
    
    private final RedisTemplate<String, String> redisTemplate;
    private static final long SESSION_TIMEOUT = 1800; // 30분
    
    public String createSession(Long userId) {
        String sessionId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(
            "SESSION:" + sessionId, 
            String.valueOf(userId), 
            SESSION_TIMEOUT, 
            TimeUnit.SECONDS
        );
        return sessionId;
    }
    
    public void removeSession(String sessionId) {
        redisTemplate.delete("SESSION:" + sessionId);
    }
    
    public Long getUserId(String sessionId) {
        String userId = redisTemplate.opsForValue().get("SESSION:" + sessionId);
        return userId != null ? Long.valueOf(userId) : null;
    }
} 