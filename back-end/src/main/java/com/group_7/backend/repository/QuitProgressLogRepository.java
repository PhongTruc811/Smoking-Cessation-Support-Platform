package com.group_7.backend.repository;

import com.group_7.backend.entity.QuitProgressLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuitProgressLogRepository extends JpaRepository<QuitProgressLog, Long> {
}