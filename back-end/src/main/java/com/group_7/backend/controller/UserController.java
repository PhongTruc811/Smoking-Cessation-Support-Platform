package com.group_7.backend.controller;

import com.group_7.backend.dto.UserDto;
import com.group_7.backend.dto.request.UserRequestDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getUser(@PathVariable long id) {
        return ResponseEntity.ok(
                new ResponseDto("success", "User fetched successfully", userService.getById(id))
        );
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllUsers() {
        return ResponseEntity.ok(
                new ResponseDto("success", "All users fetched successfully", userService.getAll())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateUser(
            @Valid @PathVariable long id,
            @RequestBody UserRequestDto userDto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "User updated successfully", userService.update(id, userDto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.ok(
                new ResponseDto("success", "User deleted successfully", null)
        );
    }
}