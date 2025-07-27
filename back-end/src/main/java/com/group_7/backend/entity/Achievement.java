package com.group_7.backend.entity;

import com.group_7.backend.entity.enums.IconTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long achievementId;
    private String name;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String icon;
    @Enumerated(EnumType.STRING)
    private IconTypeEnum iconType;
    private boolean locked;
    private String description;
    private String category;
    
    // NEW FIELDS FOR DYNAMIC RULES
    @Column(name = "rule_type")
    private String ruleType; // "DAYS_SMOKE_FREE", "MONEY_SAVED", "CIGARETTES_AVOIDED"
    
    @Column(name = "target_value")
    private Integer targetValue; // The threshold to achieve
    
    @Column(name = "comparison_operator") 
    private String comparisonOperator; // ">=", ">", "=", "<=", "<"

    public Achievement(String name, String icon, IconTypeEnum iconType, boolean locked, String description, String category) {
        this.name = name;
        this.icon = icon;
        this.iconType = iconType;
        this.locked = locked;
        this.description = description;
        this.category = category;
    }

    public Achievement(String name, String icon, IconTypeEnum iconType, boolean locked, 
                      String description, String category, String ruleType, 
                      Integer targetValue, String comparisonOperator) {
        this.name = name;
        this.icon = icon;
        this.iconType = iconType;
        this.locked = locked;
        this.description = description;
        this.category = category;
        this.ruleType = ruleType;
        this.targetValue = targetValue;
        this.comparisonOperator = comparisonOperator;
    }
}
