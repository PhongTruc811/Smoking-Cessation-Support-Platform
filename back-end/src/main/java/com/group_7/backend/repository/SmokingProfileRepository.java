package com.group_7.backend.repository;

import com.group_7.backend.entity.SmokingProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmokingProfileRepository extends JpaRepository<SmokingProfile, Long> {
    SmokingProfile findByUser_UserId(Long userId);
    boolean existsByUser_UserId(Long userId);
}