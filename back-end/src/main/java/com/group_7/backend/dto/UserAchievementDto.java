package com.group_7.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAchievementDto {
    private Long id;
    private Long userId;
    private Long achievementId;
    private LocalDateTime createdAt;
    private AchievementDto achievement;
}