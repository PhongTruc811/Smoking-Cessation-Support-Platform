package com.group_7.backend.service;

import com.group_7.backend.dto.AchievementDto;

import java.util.List;

public interface IAchievementService extends ICRUDService<AchievementDto, AchievementDto, Long>{
    List<AchievementDto> getByCategory(String category);
    List<AchievementDto> getPublicAchievements();
    List<String> getAllCategories();
}