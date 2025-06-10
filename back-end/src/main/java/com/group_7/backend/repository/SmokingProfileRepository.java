package com.group_7.backend.repository;

import com.group_7.backend.entity.SmokingProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmokingProfileRepository extends JpaRepository<SmokingProfile, Long> {
}