package com.group_7.backend.controller;

import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.IPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin") // Một prefix chung cho các API của Admin
@PreAuthorize("hasAuthority('ROLE_ADMIN')") // Bảo vệ toàn bộ controller
public class AdminController {

    private final IPostService postService;

    public AdminController(IPostService postService) {
        this.postService = postService;
    }

    // Endpoint mới để lấy tất cả bài đăng
    @GetMapping("/posts/all")
    public ResponseEntity<ResponseDto> getAllPostsForAdmin() {
        return ResponseEntity.ok(
                new ResponseDto("success", "All posts fetched for admin", postService.adminGetAll())
        );
    }

    // Trong tương lai, bạn có thể thêm các API quản lý khác vào đây
}