package com.group_7.backend.controller;

import com.group_7.backend.dto.CommentDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.ICommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final ICommentService commentService;

    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createComment(@Valid @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Comment created successfully", commentService.create(commentDto))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Comment fetched successfully", commentService.getById(id))
        );
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ResponseDto> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Comments for post fetched successfully", commentService.getByPostId(postId))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateComment(@Valid @PathVariable Long id, @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Comment updated successfully", commentService.update(id, commentDto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteComment(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.ok(
                new ResponseDto("success", "Comment deleted successfully", null)
        );
    }
}