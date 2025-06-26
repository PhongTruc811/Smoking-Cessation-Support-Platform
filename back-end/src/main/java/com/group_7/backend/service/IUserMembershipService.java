package com.group_7.backend.service;

import com.group_7.backend.dto.UserMembershipDto;
import com.group_7.backend.entity.enums.MembershipStatusEnum;
import com.group_7.backend.service.ICRUDService;

import java.util.List;

public interface IUserMembershipService extends ICRUDService<UserMembershipDto, UserMembershipDto, Long> {
    List<UserMembershipDto> getByUserId(Long userId);
    UserMembershipDto getCurrentMembership(Long userId);
    UserMembershipDto updateStatus(Long id, MembershipStatusEnum status);
}