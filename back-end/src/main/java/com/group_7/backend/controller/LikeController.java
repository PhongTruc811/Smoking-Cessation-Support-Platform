package com.group_7.backend.controller;

import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.ILikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {
    private final ILikeService likeService;

    public LikeController(ILikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/like")
    public ResponseEntity<ResponseDto> like(@RequestParam Long postId, @RequestParam Long userId) {
        likeService.like(postId, userId);
        return ResponseEntity.ok(
                new ResponseDto("success", "Liked post successfully", null)
        );
    }

    @PostMapping("/unlike")
    public ResponseEntity<ResponseDto> unlike(@RequestParam Long postId, @RequestParam Long userId) {
        likeService.unlike(postId, userId);
        return ResponseEntity.ok(
                new ResponseDto("success", "Unliked post successfully", null)
        );
    }

    @GetMapping("/count")
    public ResponseEntity<ResponseDto> countLikes(@RequestParam Long postId) {
        int count = likeService.countLikes(postId);
        return ResponseEntity.ok(
                new ResponseDto("success", "Like count fetched successfully", count)
        );
    }

    @GetMapping("/status")
    public ResponseEntity<ResponseDto> likeStatus(@RequestParam Long postId, @RequestParam Long userId) {
        boolean liked = likeService.hasUserLiked(postId, userId);
        return ResponseEntity.ok(
                new ResponseDto("success", "Like status fetched successfully", liked)
        );
    }
}