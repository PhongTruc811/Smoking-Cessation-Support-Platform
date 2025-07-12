package com.group_7.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min = 2, max = 255, message = "Title must be between 5 and 50 characters")
    private String title;

    @NotBlank
    private String content;

    private int viewCount;
    private LocalDateTime createdAt;
    private boolean isPublished;
}