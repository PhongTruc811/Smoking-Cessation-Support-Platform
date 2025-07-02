package com.group_7.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long postId;

    @NotNull
    private Long userId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private LocalDateTime createdAt;
    private boolean isPublished;
}