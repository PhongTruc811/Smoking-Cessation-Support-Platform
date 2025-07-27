package com.group_7.backend.service.impl;

import com.group_7.backend.entity.Achievement;
import com.group_7.backend.entity.User;
import com.group_7.backend.entity.UserAchievement;
import com.group_7.backend.entity.UserRecord;
import com.group_7.backend.repository.AchievementRepository;
import com.group_7.backend.repository.UserAchievementRepository;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.repository.UserRecordRepository;
import com.group_7.backend.service.IAchievementDetectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AchievementDetectionServiceImpl implements IAchievementDetectionService {
    
    private final UserRepository userRepository;
    private final UserRecordRepository userRecordRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;

    public AchievementDetectionServiceImpl(
            UserRepository userRepository,
            UserRecordRepository userRecordRepository,
            AchievementRepository achievementRepository,
            UserAchievementRepository userAchievementRepository) {
        this.userRepository = userRepository;
        this.userRecordRepository = userRecordRepository;
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
    }

    @Override
    @Transactional
    public void checkAndAwardAchievements(Long userId) {
        UserRecord userRecord = userRecordRepository.findByUserUserId(userId);
        if (userRecord == null) return;

        // Get ALL achievements (not by category) - DYNAMIC APPROACH
        List<Achievement> allAchievements = achievementRepository.findByLockedFalse();
        
        for (Achievement achievement : allAchievements) {
            if (!userAchievementRepository.existsByUserUserIdAndAchievementAchievementId(userId, achievement.getAchievementId())) {
                if (checkAchievementRule(userRecord, achievement)) {
                    awardAchievement(userId, achievement.getAchievementId());
                }
            }
        }
    }

    private boolean checkAchievementRule(UserRecord userRecord, Achievement achievement) {
        String ruleType = achievement.getRuleType();
        Integer targetValue = achievement.getTargetValue();
        String operator = achievement.getComparisonOperator();
        
        if (ruleType == null || targetValue == null || operator == null) {
            return false; // Invalid rule configuration
        }

        int currentValue = getCurrentValue(userRecord, ruleType);
        return compareValues(currentValue, targetValue, operator);
    }

    private int getCurrentValue(UserRecord userRecord, String ruleType) {
        return switch (ruleType) {
            case "DAYS_SMOKE_FREE" -> userRecord.getTotalQuitDays();
            case "MONEY_SAVED" -> userRecord.getTotalSaveMoney().intValue();
            case "CIGARETTES_AVOIDED" -> userRecord.getTotalQuitSmokes();
            default -> 0;
        };
    }

    private boolean compareValues(int currentValue, int targetValue, String operator) {
        return switch (operator) {
            case ">=" -> currentValue >= targetValue;
            case ">" -> currentValue > targetValue;
            case "=" -> currentValue == targetValue;
            case "<=" -> currentValue <= targetValue;
            case "<" -> currentValue < targetValue;
            default -> false;
        };
    }

    @Override
    @Transactional
    public void checkTimeBasedAchievements(Long userId, int daysSmokesFree) {
        // This method is kept for backward compatibility but now uses dynamic rules
        checkAndAwardAchievements(userId);
    }

    @Override
    @Transactional
    public void checkFinancialAchievements(Long userId, BigDecimal moneySaved) {
        // This method is kept for backward compatibility but now uses dynamic rules
        checkAndAwardAchievements(userId);
    }

    @Override
    @Transactional
    public void checkHealthAchievements(Long userId) {
        // This method is kept for backward compatibility but now uses dynamic rules
        checkAndAwardAchievements(userId);
    }

    @Override
    public List<String> getNewlyAwardedAchievements(Long userId) {
        // This would track recently awarded achievements (last 24 hours)
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        return userAchievementRepository.findRecentAchievements(userId, yesterday);
    }

    private void awardAchievement(Long userId, Long achievementId) {
        User user = userRepository.findById(userId).orElse(null);
        Achievement achievement = achievementRepository.findById(achievementId).orElse(null);
        
        if (user != null && achievement != null) {
            UserAchievement userAchievement = new UserAchievement();
            userAchievement.setUser(user);
            userAchievement.setAchievement(achievement);
            userAchievement.setCreatedAt(LocalDateTime.now());
            userAchievementRepository.save(userAchievement);
        }
    }
}
