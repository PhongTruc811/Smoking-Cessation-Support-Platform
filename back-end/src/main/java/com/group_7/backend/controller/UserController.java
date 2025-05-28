package com.group_7.backend.controller;

import com.group_7.backend.dto.LoginReponseDto;
import com.group_7.backend.dto.LoginRequestDto;
import com.group_7.backend.dto.UserDto;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.service.iUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final iUserService userService;
    public UserController(iUserService userService) { this.userService = userService; }

    @PostMapping("/{id}")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto UserDto, @RequestParam String password) {
        return ResponseEntity.ok(userService.createUser(UserDto, password));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Integer id, @RequestBody UserDto UserDto) {
        return ResponseEntity.ok(userService.updateUser(id, UserDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        try {
            UserDto user = userService.authenticate(request.getUsernameOrEmail(), request.getPassword());
            LoginReponseDto res = new LoginReponseDto();
            res.setMessage("Login successful");
            // Nếu dùng JWT, tạo token ở đây và trả về luôn
            return ResponseEntity.ok(res);
        } catch (ResourceNotFoundException | IllegalArgumentException ex) {
            return ResponseEntity.status(401).body(new LoginReponseDto("Login failed: " + ex.getMessage()));
        }
    }
}