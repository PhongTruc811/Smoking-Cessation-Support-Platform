package com.group_7.backend.repository;

import com.group_7.backend.entity.UserMembership;
import com.group_7.backend.entity.enums.MembershipStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMembershipRepository extends JpaRepository<UserMembership, Long> {
    List<UserMembership> findByUserUserId(Long userId);

    Optional <UserMembership> findTopByUserUserIdAndStatusOrderByStartDateDesc(Long userId, MembershipStatusEnum status);

    List<UserMembership> findAllByStatus(MembershipStatusEnum membershipStatusEnum);
    boolean existsByUserUserIdAndStatusIn(Long userId, List<MembershipStatusEnum> statuses);

    // Thêm phương thức này
    Optional<UserMembership> findByUserUserIdAndMembershipPackageIdAndStatus(Long userId, Long packageId, MembershipStatusEnum status);

    long countByStatus(MembershipStatusEnum status);

    // Safe count for demo (returns 0 if status doesn't exist)
    @Query("SELECT COUNT(m) FROM UserMembership m WHERE m.status = :status")
    long countByStatusSafe(@Param("status") MembershipStatusEnum status);

    @Query("SELECT um FROM UserMembership um JOIN FETCH um.user WHERE um.user.userId IN :userIds AND um.status = 'ACTIVE'")
    List<UserMembership> findAllActiveMembershipsForUsers(@Param("userIds") List<Long> userIds);
}