package com.group_7.backend.controller;

import com.group_7.backend.dto.SmokingProfileDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.entity.CustomUserDetail;
import com.group_7.backend.service.ISmokingProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/smokingprofile")
public class SmokingProfileController {
    private final ISmokingProfileService smokingProfileService;

    public SmokingProfileController(ISmokingProfileService smokingProfileService) {
        this.smokingProfileService = smokingProfileService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createSmokingProfile(@Valid @RequestBody SmokingProfileDto smokingProfileDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) auth.getPrincipal();
        smokingProfileDto.setUserId(customUserDetail.getId());
        return ResponseEntity.ok(
                new ResponseDto("success", "Smoking profile created successfully",
                        smokingProfileService.create(smokingProfileDto))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getSmokingProfile(@PathVariable long id) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Smoking profile fetched successfully",
                        smokingProfileService.getById(id))
        );
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllSmokingProfiles() {
        return ResponseEntity.ok(
                new ResponseDto("success", "All smoking profiles fetched successfully",
                        smokingProfileService.getAll())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateSmokingProfile(@Valid @PathVariable long id, @RequestBody SmokingProfileDto smokingProfileDto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Smoking profile updated successfully",
                        smokingProfileService.update(id, smokingProfileDto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteSmokingProfile(@PathVariable long id) {
        smokingProfileService.delete(id);
        return ResponseEntity.ok(
                new ResponseDto("success", "Smoking profile deleted successfully", null)
        );
    }
}