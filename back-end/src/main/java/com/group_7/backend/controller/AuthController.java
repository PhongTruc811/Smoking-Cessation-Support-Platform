package com.group_7.backend.controller;

import com.group_7.backend.dto.*;
import com.group_7.backend.dto.request.LoginRequestDto;
import com.group_7.backend.dto.request.RegRequestDto;
import com.group_7.backend.dto.response.JwtResponseDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.IUserService;
import com.group_7.backend.util.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(IUserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /*trả về JWT token + user info khi đăng nhập thành công*/
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
        UserDto user = userService.authenticate(request.getUsernameOrEmail(), request.getPassword());
        if (user == null) {
            //Trả về LoginResponse chứa lỗi
            return ResponseEntity.status(401)
                    .body(new ResponseDto("success","Login failed: Invalid credentials"));
        }
        String token= jwtTokenProvider.generateToken(user.getUsername());
        return ResponseEntity.ok(new JwtResponseDto("success","Login successfully!",token,user));//Trả về JwtResponse chứa token và user info
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@Valid @RequestBody RegRequestDto request) {
        UserDto user = userService.register(request);
        return ResponseEntity.ok(
                new ResponseDto("success", "User registered successfully", user)
        );
    }

    //curl -v -X POST http://localhost:8080/api/login -H "Content-Type: application/json" -d "{\"usernameOrEmail\":\"testuser\", \"password\":\"123456\"}"
}