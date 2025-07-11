package com.group_7.backend.controller;

import com.group_7.backend.dto.QuitPlanDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.entity.CustomUserDetail;
import com.group_7.backend.service.IQuitPlanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quitplans")
public class QuitPlanController {
    private final IQuitPlanService quitPlanService;

    public QuitPlanController(IQuitPlanService quitPlanService) {
        this.quitPlanService = quitPlanService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createQuitPlan(@Valid @RequestBody QuitPlanDto quitPlanDto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Quit plan created successfully", quitPlanService.create(quitPlanDto))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getQuitPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Quit plan fetched successfully", quitPlanService.getById(id))
        );
    }

    @GetMapping("/current")
    public ResponseEntity<ResponseDto> getCurrentQuitPlan() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) auth.getPrincipal();
        return ResponseEntity.ok(
                new ResponseDto("success", "Quit plan fetched successfully", quitPlanService.getCurrentByUserIdAndStatus(customUserDetail.getId()))
        );
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllQuitPlans() {
        return ResponseEntity.ok(
                new ResponseDto("success", "All quit plans fetched successfully", quitPlanService.getAll())
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDto> getQuitPlansByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Quit plans by user fetched successfully", quitPlanService.getByUserId(userId))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateQuitPlan(@Valid @PathVariable Long id, @RequestBody QuitPlanDto quitPlanDto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Quit plan updated successfully", quitPlanService.update(id, quitPlanDto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteQuitPlan(@PathVariable Long id) {
        quitPlanService.delete(id);
        return ResponseEntity.ok(
                new ResponseDto("success", "Quit plan deleted successfully", null)
        );
    }


}