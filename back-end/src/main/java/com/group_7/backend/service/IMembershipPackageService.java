package com.group_7.backend.service;

import com.group_7.backend.dto.MembershipPackageDto;

public interface IMembershipPackageService extends ICRUDService<MembershipPackageDto, MembershipPackageDto, Long> {
    MembershipPackageDto changeActive(Long id);
}
