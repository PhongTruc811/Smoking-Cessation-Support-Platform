package com.group_7.backend.mapper;

import com.group_7.backend.dto.AchievementDto;
import com.group_7.backend.entity.Achievement;
import org.springframework.stereotype.Component;

@Component
public class AchievementMapper {
    public AchievementDto toDto(Achievement entity) {
        if (entity == null) return null;
        AchievementDto dto= new AchievementDto();
        dto.setAchievementId(entity.getAchievementId());
        dto.setDescription(entity.getDescription());
        dto.setName(entity.getName());
        dto.setIcon(entity.getIcon());
        dto.setIconType(entity.getIconType());
        dto.setLocked(entity.isLocked());
        dto.setCategory(entity.getCategory());
        // Add new dynamic rule fields
        dto.setRuleType(entity.getRuleType());
        dto.setTargetValue(entity.getTargetValue());
        dto.setComparisonOperator(entity.getComparisonOperator());
        return dto;
    }

    public Achievement toEntity(AchievementDto dto) {
        if (dto == null) return null;
        Achievement entity = new Achievement();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setIcon(dto.getIcon());
        entity.setIconType(dto.getIconType());
        entity.setCategory(dto.getCategory());
        entity.setLocked(dto.isLocked());
        // Add new dynamic rule fields
        entity.setRuleType(dto.getRuleType());
        entity.setTargetValue(dto.getTargetValue());
        entity.setComparisonOperator(dto.getComparisonOperator());
        return entity;
    }
}