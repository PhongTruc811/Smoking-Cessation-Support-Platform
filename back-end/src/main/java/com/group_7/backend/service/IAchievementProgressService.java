package com.group_7.backend.service;

import com.group_7.backend.dto.AchievementProgressDto;

import java.util.List;

public interface IAchievementProgressService {
    List<AchievementProgressDto> getUserAchievementProgress(Long userId);
    AchievementProgressDto getAchievementProgress(Long userId, Long achievementId);
}
