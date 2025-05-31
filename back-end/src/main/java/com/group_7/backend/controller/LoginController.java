package com.group_7.backend.controller;

import com.group_7.backend.dto.LoginReponseDto;
import com.group_7.backend.dto.LoginRequestDto;
import com.group_7.backend.dto.RegRequestDto;
import com.group_7.backend.dto.UserDto;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final IUserService userService;
    public LoginController(IUserService userService) { this.userService = userService; }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        try {
            UserDto user = userService.authenticate(request.getUsernameOrEmail(), request.getPassword());
            if (user == null) {
                return ResponseEntity.status(401).body(new LoginReponseDto("Login failed: Invalid credentials", null));
            }
            // Nếu có JWT, tao token ở đây, thêm vào response
            LoginReponseDto res = new LoginReponseDto();
            res.setMessage("Login successful");
            res.setUser(user);
            System.out.println(user);
            return ResponseEntity.ok(res);
        } catch (ResourceNotFoundException | IllegalArgumentException ex) {
            return ResponseEntity.status(401).body(new LoginReponseDto("Login failed: " + ex.getMessage(),null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegRequestDto request) {
        try {
            UserDto user = userService.register(request);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    //curl -v -X POST http://localhost:8080/api/login -H "Content-Type: application/json" -d "{\"usernameOrEmail\":\"testuser\", \"password\":\"123456\"}"
}