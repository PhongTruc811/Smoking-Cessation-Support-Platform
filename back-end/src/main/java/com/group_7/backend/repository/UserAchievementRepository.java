package com.group_7.backend.repository;

import com.group_7.backend.entity.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    List<UserAchievement> findByUserUserId(Long userId);
    boolean existsByUserUserIdAndAchievementAchievementId(Long userId, Long achievementId);

    @Query("SELECT COUNT(ua) FROM UserAchievement ua WHERE ua.achievement.achievementId = :achievementId")
    long countByAchievementId(@Param("achievementId") Long achievementId);
    
    @Query("SELECT ua.achievement.name FROM UserAchievement ua WHERE ua.user.userId = :userId AND ua.createdAt >= :since")
    List<String> findRecentAchievements(@Param("userId") Long userId, @Param("since") LocalDateTime since);
    
    @Query("SELECT ua FROM UserAchievement ua WHERE ua.user.userId = :userId AND ua.achievement.category = :category")
    List<UserAchievement> findByUserIdAndCategory(@Param("userId") Long userId, @Param("category") String category);
}