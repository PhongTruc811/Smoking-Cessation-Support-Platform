package com.group_7.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AchievementProgressDto {
    private Long achievementId;
    private String achievementName;
    private String description;
    private String category;
    private String icon;
    private boolean isEarned;
    private int currentProgress;
    private int targetProgress;
    private double progressPercentage;
    private String progressText; // e.g., "5/30 days" or "$50/$100"
}
