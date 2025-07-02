package com.group_7.backend.controller;

import com.group_7.backend.dto.UserAchievementDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.IUserAchievementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-achievements")
public class UserAchievementController {

    private final IUserAchievementService userAchievementService;

    public UserAchievementController(IUserAchievementService userAchievementService) {
        this.userAchievementService = userAchievementService;
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getUserAchievements() {
        List<UserAchievementDto> achievements = userAchievementService.getAll();
        return ResponseEntity.ok(
                new ResponseDto("success", "All user achievements fetched successfully", achievements)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getUserAchievement(@PathVariable Long id) {
        UserAchievementDto dto = userAchievementService.getById(id);
        return ResponseEntity.ok(
                new ResponseDto("success", "User achievement fetched successfully", dto)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDto> getUserAchievementsByUserId(@PathVariable Long userId) {
        List<UserAchievementDto> achievements = userAchievementService.getByUserId(userId);
        return ResponseEntity.ok(
                new ResponseDto("success", "User achievements fetched successfully", achievements)
        );
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createUserAchievement(@Valid @RequestBody UserAchievementDto userAchievementDto) {
        UserAchievementDto created = userAchievementService.create(userAchievementDto);
        return ResponseEntity.ok(
                new ResponseDto("success", "User achievement created successfully", created)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteUserAchievement(@PathVariable Long id) {
        userAchievementService.delete(id);
        return ResponseEntity.ok(
                new ResponseDto("success", "User achievement deleted successfully", null)
        );
    }
}