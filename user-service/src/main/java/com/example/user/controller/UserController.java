package com.example.user.controller;

import com.example.common.dto.ApiResponse;
import com.example.user.dto.SignupRequest;
import com.example.user.dto.UserResponse;
import com.example.user.dto.UserValidationRequest;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<UserResponse> signup(@RequestBody SignupRequest request) {
        UserResponse user = userService.signup(request);
        return ApiResponse.success(user);
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ApiResponse.success(user);
    }

    @PostMapping("/validate")
    public ApiResponse<UserResponse> validateUser(@RequestBody UserValidationRequest request) {
        UserResponse user = userService.validateUser(request.getEmail(), request.getPassword());
        return ApiResponse.success(user);
    }
    
    @GetMapping("/email/{email}")
    public ApiResponse<UserResponse> getUserByEmail(@PathVariable String email) {
        UserResponse user = userService.getUserByEmail(email);
        return ApiResponse.success(user);
    }
} 