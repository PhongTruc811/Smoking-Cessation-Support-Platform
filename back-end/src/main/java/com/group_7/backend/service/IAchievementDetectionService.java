package com.group_7.backend.service;

import java.math.BigDecimal;
import java.util.List;

public interface IAchievementDetectionService {
    void checkAndAwardAchievements(Long userId);
    void checkTimeBasedAchievements(Long userId, int daysSmokesFree);
    void checkFinancialAchievements(Long userId, BigDecimal moneySaved);
    void checkHealthAchievements(Long userId);
    List<String> getNewlyAwardedAchievements(Long userId);
}
