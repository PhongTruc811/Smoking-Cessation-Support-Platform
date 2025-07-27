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
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String icon;
    private IconTypeEnum iconType;
    private boolean locked;
    private String description;
    private String category;
    
    // NEW FIELDS FOR DYNAMIC RULES
    private String ruleType; // "DAYS_SMOKE_FREE", "MONEY_SAVED", "CIGARETTES_AVOIDED"
    private Integer targetValue; // The threshold to achieve
    private String comparisonOperator; // ">=", ">", "=", "<=", "<"
}
