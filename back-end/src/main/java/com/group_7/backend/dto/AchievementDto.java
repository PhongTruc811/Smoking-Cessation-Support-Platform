package com.group_7.backend.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public class AchievementDto {
    private long achievementId;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Description must not be blank")
    private String description;

    @NotBlank(message = "ImageUrl must not be blank")
    private String imageUrl;
}
