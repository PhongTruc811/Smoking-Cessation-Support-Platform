package com.group_7.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long commentId;
    private Long parentCommentId;

    @NotNull
    private Long userId;

    @NotNull
    private Long postId;

    @NotBlank
    private String content;

    private LocalDateTime createdAt;
}