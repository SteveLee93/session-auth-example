package com.example.auth.controller;

import com.example.common.dto.ApiResponse;
import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.LoginResponse;
import com.example.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private static final int COOKIE_MAX_AGE = 1800; // 30분

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        
        ResponseCookie cookie = ResponseCookie.from("SESSION", response.getSessionId())
                .path("/")
                .maxAge(COOKIE_MAX_AGE)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(ApiResponse.success(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@CookieValue(name = "SESSION", required = false) String sessionId) {
        if (sessionId != null) {
            authService.logout(sessionId);
        }
        
        ResponseCookie cookie = ResponseCookie.from("SESSION", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(ApiResponse.success(null));
    }
} 