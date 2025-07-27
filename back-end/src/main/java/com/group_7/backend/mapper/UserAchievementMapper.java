package com.group_7.backend.mapper;

import com.group_7.backend.dto.UserAchievementDto;
import com.group_7.backend.entity.Achievement;
import com.group_7.backend.entity.User;
import com.group_7.backend.entity.UserAchievement;
import org.springframework.stereotype.Component;

@Component
public class UserAchievementMapper {

    private final AchievementMapper achievementMapper;

    public UserAchievementMapper(AchievementMapper achievementMapper) {
        this.achievementMapper = achievementMapper;
    }

    public UserAchievementDto toDto(UserAchievement entity) {
        if (entity == null) return null;
        UserAchievementDto dto = new UserAchievementDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getUserId());
        dto.setAchievementId(entity.getAchievement().getAchievementId()); // Add this line
        dto.setCreatedAt(entity.getCreatedAt()); // Add this line
        dto.setAchievement(achievementMapper.toDto(entity.getAchievement()));
        return dto;
    }

    public UserAchievement toEntity(User user, Achievement achievement) {
        UserAchievement entity = new UserAchievement();
        entity.setUser(user);
        entity.setAchievement(achievement);
        return entity;
    }
}