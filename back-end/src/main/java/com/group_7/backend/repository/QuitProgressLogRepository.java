package com.group_7.backend.repository;

import com.group_7.backend.entity.QuitProgressLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface QuitProgressLogRepository extends JpaRepository<QuitProgressLog, Long> {
    boolean existsByCreatedAtAndQuitPlan_Id(LocalDate createdAt, Long id);
}