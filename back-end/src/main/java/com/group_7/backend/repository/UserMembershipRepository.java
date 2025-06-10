package com.group_7.backend.repository;

import com.group_7.backend.entity.UserMembership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMembershipRepository extends JpaRepository<UserMembership, Long> {
}