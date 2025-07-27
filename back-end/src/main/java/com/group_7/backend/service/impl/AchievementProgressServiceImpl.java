package com.group_7.backend.service.impl;

import com.group_7.backend.dto.AchievementProgressDto;
import com.group_7.backend.entity.Achievement;
import com.group_7.backend.entity.User;
import com.group_7.backend.entity.UserRecord;
import com.group_7.backend.repository.AchievementRepository;
import com.group_7.backend.repository.UserAchievementRepository;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.repository.UserRecordRepository;
import com.group_7.backend.service.IAchievementProgressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementProgressServiceImpl implements IAchievementProgressService {
    
    private final UserRepository userRepository;
    private final UserRecordRepository userRecordRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;

    public AchievementProgressServiceImpl(
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
    public List<AchievementProgressDto> getUserAchievementProgress(Long userId) {
        List<Achievement> allAchievements = achievementRepository.findByLockedFalse();
        return allAchievements.stream()
                .map(achievement -> getAchievementProgress(userId, achievement.getAchievementId()))
                .collect(Collectors.toList());
    }

    @Override
    public AchievementProgressDto getAchievementProgress(Long userId, Long achievementId) {
        Achievement achievement = achievementRepository.findById(achievementId).orElse(null);
        if (achievement == null) return null;

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;

        boolean isEarned = userAchievementRepository.existsByUserUserIdAndAchievementAchievementId(userId, achievementId);
        
        AchievementProgressDto progress = new AchievementProgressDto();
        progress.setAchievementId(achievementId);
        progress.setAchievementName(achievement.getName());
        progress.setDescription(achievement.getDescription());
        progress.setCategory(achievement.getCategory());
        progress.setIcon(achievement.getIcon());
        progress.setEarned(isEarned);

        if (isEarned) {
            progress.setCurrentProgress(100);
            progress.setTargetProgress(100);
            progress.setProgressPercentage(100.0);
            progress.setProgressText("Completed");
            return progress;
        }

        // Calculate progress using UserRecord data and dynamic rules
        calculateProgress(user, achievement, progress);
        return progress;
    }

    private void calculateProgress(User user, Achievement achievement, AchievementProgressDto progress) {
        UserRecord userRecord = userRecordRepository.findByUserUserId(user.getUserId());
        if (userRecord == null) return;

        // DYNAMIC CALCULATION BASED ON RULE TYPE
        String ruleType = achievement.getRuleType();
        Integer targetValue = achievement.getTargetValue();
        
        if (ruleType == null || targetValue == null) {
            progress.setProgressText("Invalid rule configuration");
            return;
        }

        int currentValue = getCurrentValueByRuleType(userRecord, ruleType);
        String unit = getUnitByRuleType(ruleType);
        
        progress.setCurrentProgress(Math.min(currentValue, targetValue));
        progress.setTargetProgress(targetValue);
        progress.setProgressPercentage(targetValue > 0 ? (double) currentValue / targetValue * 100 : 0);
        progress.setProgressText(currentValue + "/" + targetValue + " " + unit);
    }

    private int getCurrentValueByRuleType(UserRecord userRecord, String ruleType) {
        return switch (ruleType) {
            case "DAYS_SMOKE_FREE" -> userRecord.getTotalQuitDays();
            case "MONEY_SAVED" -> userRecord.getTotalSaveMoney().intValue();
            case "CIGARETTES_AVOIDED" -> userRecord.getTotalQuitSmokes();
            default -> 0;
        };
    }

    private String getUnitByRuleType(String ruleType) {
        return switch (ruleType) {
            case "DAYS_SMOKE_FREE" -> "days";
            case "MONEY_SAVED" -> "dollars";
            case "CIGARETTES_AVOIDED" -> "cigarettes";
            default -> "units";
        };
    }
}
