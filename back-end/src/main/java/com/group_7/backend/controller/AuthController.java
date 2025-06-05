package com.group_7.backend.controller;

import com.group_7.backend.dto.LoginRequestDto;
import com.group_7.backend.dto.LoginResponseDto;
import com.group_7.backend.dto.RegRequestDto;
import com.group_7.backend.dto.UserDto;
import com.group_7.backend.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final IUserService userService;

    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        UserDto user = userService.authenticate(request.getUsernameOrEmail(), request.getPassword());
        if (user == null) {
            return ResponseEntity.status(401)
                    .body(new LoginResponseDto("Login failed: Invalid credentials", null));
        }
        return ResponseEntity.ok(new LoginResponseDto("Login successful", user));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegRequestDto request) {
        UserDto user = userService.register(request);
        return ResponseEntity.ok(user);
    }

    //curl -v -X POST http://localhost:8080/api/login -H "Content-Type: application/json" -d "{\"usernameOrEmail\":\"testuser\", \"password\":\"123456\"}"
}