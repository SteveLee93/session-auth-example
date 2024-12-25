package com.example.auth.client;

import com.example.auth.dto.UserDto;
import com.example.auth.dto.UserValidationRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserServiceClient {
    
    private final RestTemplate restTemplate;
    private static final String USER_SERVICE_URL = "http://user-service:8002";
    
    public UserDto validateUser(String email, String password) {
        // POST 요청으로 사용자 검증
        return restTemplate.postForObject(
            USER_SERVICE_URL + "/users/validate",
            new UserValidationRequest(email, password),
            UserDto.class
        );
    }
    
    public UserDto getUserByEmail(String email) {
        return restTemplate.getForObject(
            USER_SERVICE_URL + "/users/email/{email}",
            UserDto.class,
            email
        );
    }
} 