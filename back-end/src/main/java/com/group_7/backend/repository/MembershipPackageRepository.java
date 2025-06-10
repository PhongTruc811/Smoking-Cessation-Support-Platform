package com.group_7.backend.repository;

import com.group_7.backend.entity.MembershipPackage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipPackageRepository extends JpaRepository<MembershipPackage, Long> {
}