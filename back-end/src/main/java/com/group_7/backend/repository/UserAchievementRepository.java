package com.group_7.backend.repository;

import com.group_7.backend.entity.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    List<UserAchievement> findByUserUserId(Long userId);
    boolean existsByUserUserIdAndAchievementAchievementId(Long userId, Long achievementId);
}