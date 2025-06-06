package com.group_7.backend.controller;

import com.group_7.backend.dto.*;
import com.group_7.backend.service.IUserService;
import com.group_7.backend.util.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final IUserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(IUserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
        UserDto user = userService.authenticate(request.getUsernameOrEmail(), request.getPassword());
        if (user == null) {
            //Trả về LoginResponse chứa lỗi
            return ResponseEntity.status(401)
                    .body(new LoginResponseDto("Login failed: Invalid credentials", null));
        }
        String token= jwtTokenProvider.generateToken(user.getUsername());
        return ResponseEntity.ok(new JwtResponseDto(token));//Trả về Response chứa token
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegRequestDto request) {
        UserDto user = userService.register(request);
        return ResponseEntity.ok(user);
    }

    //curl -v -X POST http://localhost:8080/api/login -H "Content-Type: application/json" -d "{\"usernameOrEmail\":\"testuser\", \"password\":\"123456\"}"
}