package com.group_7.backend.controller;

import com.group_7.backend.dto.PostDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.IPostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final IPostService postService;

    public PostController(IPostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createPost(@Valid @RequestBody PostDto postDto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Post created successfully", postService.create(postDto))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Post fetched successfully", postService.getById(id))
        );
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllPosts() {
        return ResponseEntity.ok(
                new ResponseDto("success", "All posts fetched successfully", postService.getAll())
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDto> getPostsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Posts by user fetched successfully", postService.getByUserId(userId))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updatePost(@Valid @PathVariable Long id, @RequestBody PostDto postDto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Post updated successfully", postService.update(id, postDto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deletePost(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.ok(
                new ResponseDto("success", "Post deleted successfully", null)
        );
    }
    @GetMapping("/stats")
    public ResponseEntity<ResponseDto> getStats() {
        return ResponseEntity.ok(
                new ResponseDto("success", "Stats fetched successfully", postService.getStats())
        );
    }
}