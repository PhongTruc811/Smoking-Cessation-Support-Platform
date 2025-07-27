package com.group_7.backend.repository;

import com.group_7.backend.entity.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRecordRepository extends JpaRepository<UserRecord, Long> {
    UserRecord findByUserUserId(Long userId);
}
