package com.group_7.backend.controller;

import com.group_7.backend.dto.AchievementDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.IAchievementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    private final IAchievementService achievementService;

    public AchievementController(IAchievementService achievementService) {
        this.achievementService = achievementService;
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
        return ResponseEntity.ok(
                new ResponseDto("success","Achievement deleted successfully", null)
        );
    }

}
