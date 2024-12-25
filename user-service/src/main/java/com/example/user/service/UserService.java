package com.example.user.service;

import com.example.common.exception.BaseException;
import com.example.user.domain.User;
import com.example.user.dto.SignupRequest;
import com.example.user.dto.UserResponse;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional
    public UserResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BaseException("이미 존재하는 이메일입니다.");
        }
        
        User user = new User(
            request.getEmail(),
            request.getPassword(), // 실제로는 암호화해야 함
            request.getName()
        );
        
        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }
    
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new BaseException("사용자를 찾을 수 없습니다."));
        return new UserResponse(user);
    }
    
    public UserResponse validateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BaseException("사용자를 찾을 수 없습니다."));
        
        // 실제로는 암호화된 비밀번호와 비교해야 함
        if (!user.getPassword().equals(password)) {
            throw new BaseException("비밀번호가 일치하지 않습니다.");
        }
        
        return new UserResponse(user);
    }
    
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BaseException("사용자를 찾을 수 없습니다."));
        return new UserResponse(user);
    }
} 