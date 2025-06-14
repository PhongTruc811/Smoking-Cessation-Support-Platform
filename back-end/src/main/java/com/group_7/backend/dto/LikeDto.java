package com.group_7.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeDto {
    private Long likeId;

    @NotNull
    private Long userId;

    private Long postId;
    private Long commentId;
    private LocalDateTime createdAt;
}