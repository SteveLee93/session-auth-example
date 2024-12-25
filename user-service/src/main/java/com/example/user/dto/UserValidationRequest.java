package com.example.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserValidationRequest {
    private String email;
    private String password;
} 