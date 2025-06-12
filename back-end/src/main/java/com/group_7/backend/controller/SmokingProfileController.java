package com.group_7.backend.controller;

import com.group_7.backend.dto.SmokingProfileDto;
import com.group_7.backend.service.ISmokingProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/smokingprofile")
public class SmokingProfileController {
    private final ISmokingProfileService smokingProfileService;

    public SmokingProfileController(ISmokingProfileService smokingProfileService) {
        this.smokingProfileService = smokingProfileService;
    }

    @PostMapping
    public ResponseEntity<SmokingProfileDto> createUser(@Valid @RequestBody SmokingProfileDto smokingProfileDto, @RequestParam String password) {
        return ResponseEntity.ok(smokingProfileService.create(smokingProfileDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SmokingProfileDto> getUser(@PathVariable long id) {
        return ResponseEntity.ok(smokingProfileService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<SmokingProfileDto>> getAllUsers() {
        return ResponseEntity.ok(smokingProfileService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SmokingProfileDto> updateUser(@Valid @PathVariable long id, @RequestBody SmokingProfileDto smokingProfileDto) {
        return ResponseEntity.ok(smokingProfileService.update(id, smokingProfileDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        smokingProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
