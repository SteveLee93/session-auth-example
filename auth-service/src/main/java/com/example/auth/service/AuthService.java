package com.example.auth.service;

import com.example.auth.client.UserServiceClient;
import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.LoginResponse;
import com.example.auth.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final SessionService sessionService;
    private final UserServiceClient userServiceClient;
    
    public LoginResponse login(LoginRequest request) {
        // 1. User Service에 사용자 검증 요청
        UserDto user = userServiceClient.validateUser(
            request.getEmail(), 
            request.getPassword()
        );
        
        // 2. 세션 생성
        String sessionId = sessionService.createSession(user.getId());
        
        // 3. 응답 생성
        return LoginResponse.builder()
                .sessionId(sessionId)
                .user(user)
                .build();
    }
    
    public void logout(String sessionId) {
        sessionService.removeSession(sessionId);
    }
} 