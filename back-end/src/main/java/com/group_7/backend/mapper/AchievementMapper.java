package com.group_7.backend.mapper;

import com.group_7.backend.dto.AchievementDto;
import com.group_7.backend.entity.Achievement;
import org.springframework.stereotype.Component;

@Component
public class AchievementMapper {
    public AchievementDto toDto(Achievement entity) {
        if (entity == null) return null;
        return new AchievementDto(
                entity.getAchievementId(),
                entity.getName(),
                entity.getDescription(),
                entity.getImageUrl()
        );
    }

    public Achievement toEntity(AchievementDto dto) {
        if (dto == null) return null;
        Achievement entity = new Achievement();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setImageUrl(dto.getImageUrl());
        return entity;
    }
}