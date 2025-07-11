package com.group_7.backend.repository;

import com.group_7.backend.entity.QuitPlan;
import com.group_7.backend.entity.enums.QuitPlanStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuitPlanRepository extends JpaRepository<QuitPlan, Long> {
    List<QuitPlan> findByUserUserId(Long userId);
    List<QuitPlan> findByUserUserIdAndStatus(Long userId, QuitPlanStatusEnum status);
    boolean existsByUserUserIdAndStatus(Long userId, QuitPlanStatusEnum status);
    Optional<QuitPlan> findTopByUserUserIdAndStatus(Long userId, QuitPlanStatusEnum status);

    boolean existsByCreatedAt(LocalDate createdAt);
}