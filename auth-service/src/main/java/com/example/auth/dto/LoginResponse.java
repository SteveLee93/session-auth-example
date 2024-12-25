package com.example.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String sessionId;
    private UserDto user;
} 