package com.group_7.backend.controller;

import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.IAdminDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin") // <-- add /api prefix here
public class AdminDashboardController {

    private final IAdminDashboardService dashboardService;

    public AdminDashboardController(IAdminDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard-stats")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> getDashboardStats() {
        return ResponseEntity.ok(
                new ResponseDto("success", "Dashboard stats fetched successfully", dashboardService.getDashboardStats())
        );
    }
}