package com.group_7.backend.controller;

import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.IAdminService;
import com.group_7.backend.service.IPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map; // <-- Thêm import này

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    // --- SỬA LỖI 1: KHAI BÁO ĐẦY ĐỦ CÁC SERVICE ---
    private final IPostService postService;
    private final IAdminService adminService; // <-- Bỏ comment dòng này

    // --- SỬA LỖI 2: MỘT HÀM KHỞI TẠO DUY NHẤT ---
    public AdminController(IPostService postService, IAdminService adminService) {
        this.postService = postService;
        this.adminService = adminService; // <-- Bỏ comment dòng này
    }

    // Endpoint để lấy tất cả bài đăng
    @GetMapping("/posts/all")
    public ResponseEntity<ResponseDto> getAllPostsForAdmin() {
        return ResponseEntity.ok(
                new ResponseDto("success", "All posts fetched for admin", postService.adminGetAll())
        );
    }

    // Endpoint lấy tất cả user với thông tin membership
    @GetMapping("/users/all")
    public ResponseEntity<ResponseDto> getAllUsersForAdmin() {
        // Giờ đây adminService không còn là null
        return ResponseEntity.ok(
                new ResponseDto("success", "Users fetched for admin", adminService.getAllUsersWithMembership())
        );
    }

    // Endpoint thay đổi trạng thái user
    @PutMapping("/users/{userId}/change-status")
    public ResponseEntity<ResponseDto> changeUserStatus(@PathVariable Long userId) {
        return ResponseEntity.ok(
                new ResponseDto("success", "User status changed", adminService.changeUserStatus(userId))
        );
    }

    // Endpoint thay đổi vai trò user
    @PutMapping("/users/{userId}/change-role")
    public ResponseEntity<ResponseDto> changeUserRole(@PathVariable Long userId, @RequestBody Map<String, String> payload) {
        String newRole = payload.get("role");
        return ResponseEntity.ok(
                new ResponseDto("success", "User role changed", adminService.changeUserRole(userId, newRole))
        );
    }
}