package com.group_7.backend.repository;

import com.group_7.backend.entity.UserMembership;
import com.group_7.backend.entity.enums.MembershipStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMembershipRepository extends JpaRepository<UserMembership, Long> {
    List<UserMembership> findByUserId(Long userId);

    Optional<UserMembership> findTopByUserIdAndStatusOrderByStartDateDesc(Long userId, MembershipStatusEnum status);
}