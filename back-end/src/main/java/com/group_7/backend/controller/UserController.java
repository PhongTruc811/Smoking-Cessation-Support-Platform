package com.group_7.backend.controller;

import com.group_7.backend.dto.UserDto;
import com.group_7.backend.dto.request.PasswordRequestDto;
import com.group_7.backend.dto.request.UserRequestDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.entity.CustomUserDetail;
import com.group_7.backend.entity.enums.UserRoleEnum;
import com.group_7.backend.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/current-user")
    public ResponseEntity<ResponseDto> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) auth.getPrincipal();
        return ResponseEntity.ok(
                new ResponseDto("success", "User fetched successfully", userService.getById(customUserDetail.getId())));
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllUsers() {
        return ResponseEntity.ok(
                new ResponseDto("success", "All users fetched successfully", userService.getAll())
        );
    }


    @PutMapping("/{id}/update-profile")
    public ResponseEntity<ResponseDto> updateUser(@PathVariable long id,@Valid @RequestBody UserRequestDto userDto) {
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

    @PutMapping("/{id}/change-status")
    public ResponseEntity<ResponseDto> changeUserStatus(@PathVariable long id) {
        return ResponseEntity.ok(
                new ResponseDto("success", "User updated successfully", userService.changeStatus(id))
        );
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<ResponseDto> changePassword(@PathVariable long id,@Valid @RequestBody PasswordRequestDto passwordDto) {
        userService.changePassword(id, passwordDto);
        return ResponseEntity.ok(new ResponseDto("success","User updated successfully",null));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<ResponseDto> getAllByRole(@PathVariable UserRoleEnum role) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Users with role " + role + " fetched successfully", userService.getAllByRole(role))
        );
    }
}