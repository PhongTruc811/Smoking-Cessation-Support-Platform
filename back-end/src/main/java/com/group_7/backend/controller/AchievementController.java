package com.group_7.backend.controller;

import com.group_7.backend.dto.AchievementDto;
import com.group_7.backend.dto.AchievementProgressDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.IAchievementService;
import com.group_7.backend.service.IAchievementDetectionService;
import com.group_7.backend.service.IAchievementProgressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    private final IAchievementService achievementService;
    private final IAchievementDetectionService achievementDetectionService;
    private final IAchievementProgressService achievementProgressService;

    public AchievementController(
            IAchievementService achievementService,
            IAchievementDetectionService achievementDetectionService,
            IAchievementProgressService achievementProgressService) {
        this.achievementService = achievementService;
        this.achievementDetectionService = achievementDetectionService;
        this.achievementProgressService = achievementProgressService;
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAchievements() {
        return ResponseEntity.ok(
                new ResponseDto("success","All achievements fetched successfully", achievementService.getAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getAchievement(@PathVariable long id) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Achievement fetched successfully", achievementService.getById(id))
        );
    }

    @GetMapping("/categories/{category}")
    public ResponseEntity<ResponseDto> getAchievementsByCategory(@PathVariable String category) {
        List<AchievementDto> achievements = achievementService.getByCategory(category);
        return ResponseEntity.ok(
                new ResponseDto("success", "Achievements by category fetched successfully", achievements)
        );
    }

    @GetMapping("/categories")
    public ResponseEntity<ResponseDto> getAllCategories() {
        List<String> categories = achievementService.getAllCategories();
        return ResponseEntity.ok(
                new ResponseDto("success", "Achievement categories fetched successfully", categories)
        );
    }

    @GetMapping("/public")
    public ResponseEntity<ResponseDto> getPublicAchievements() {
        List<AchievementDto> achievements = achievementService.getPublicAchievements();
        return ResponseEntity.ok(
                new ResponseDto("success", "Public achievements fetched successfully", achievements)
        );
    }

    @GetMapping("/user/{userId}/progress")
    public ResponseEntity<ResponseDto> getUserAchievementProgress(@PathVariable Long userId) {
        List<AchievementProgressDto> progress = achievementProgressService.getUserAchievementProgress(userId);
        return ResponseEntity.ok(
                new ResponseDto("success", "User achievement progress fetched successfully", progress)
        );
    }

    @PostMapping("/user/{userId}/check")
    public ResponseEntity<ResponseDto> checkUserAchievements(@PathVariable Long userId) {
        achievementDetectionService.checkAndAwardAchievements(userId);
        List<String> newAchievements = achievementDetectionService.getNewlyAwardedAchievements(userId);
        return ResponseEntity.ok(
                new ResponseDto("success", "Achievements checked successfully", newAchievements)
        );
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createAchievement(@Valid @RequestBody AchievementDto achievementDto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Achievement created successfully", achievementService.create(achievementDto))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateAchievement(@PathVariable long id,@Valid @RequestBody AchievementDto achievementDto) {
        return ResponseEntity.ok(
                new ResponseDto("success","Achievement updated successfully", achievementService.update(id, achievementDto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteAchievement(@PathVariable long id) {
        achievementService.delete(id);
        return ResponseEntity.ok(
                new ResponseDto("success","Achievement deleted successfully", null)
        );
    }

}
