package com.group_7.backend.repository;

import com.group_7.backend.entity.QuitPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuitPlanRepository extends JpaRepository<QuitPlan, Long> {
    List<QuitPlan> findByUserUserId(Long userId);
}