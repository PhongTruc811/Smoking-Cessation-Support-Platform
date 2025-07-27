package com.group_7.backend.repository;

import com.group_7.backend.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    
    List<Achievement> findByCategory(String category);
    
    List<Achievement> findByLockedFalse(); // Public achievements
    
    List<Achievement> findByLockedTrue();  // Locked achievements
    
    @Query("SELECT a FROM Achievement a WHERE a.category = :category AND a.locked = false")
    List<Achievement> findPublicAchievementsByCategory(@Param("category") String category);
    
    @Query("SELECT DISTINCT a.category FROM Achievement a")
    List<String> findAllCategories();
}