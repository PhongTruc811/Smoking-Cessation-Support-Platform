package com.group_7.backend.dto;

import com.group_7.backend.entity.enums.IconTypeEnum;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AchievementDto {
    private Long achievementId;
    private String name;
    private String icon;
    private IconTypeEnum iconType;
    private boolean locked;
    private String description;
    private String category;
}
