package com.group_7.backend.mapper;

import com.group_7.backend.dto.UserMembershipDto;
import com.group_7.backend.entity.MembershipPackage;
import com.group_7.backend.entity.User;
import com.group_7.backend.entity.UserMembership;
import org.springframework.stereotype.Component;

@Component
public class UserMembershipMapper {
    // Entity -> DTO
    public UserMembershipDto toDto(UserMembership entity) {
        if (entity == null) return null;
        UserMembershipDto dto = new UserMembershipDto();
        dto.setId(entity.getId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setStatus(entity.getStatus());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : 0);
        dto.setMembershipPackageId(entity.getMembershipPackage() != null ? entity.getMembershipPackage().getId() : 0);
        return dto;
    }

    // DTO -> Entity (cần truyền User và MembershipPackage)
    public UserMembership toEntity(UserMembershipDto dto, User user, MembershipPackage membershipPackage) {
        if (dto == null) return null;
        UserMembership entity = new UserMembership();
        entity.setId(dto.getId());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setStatus(dto.getStatus());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setUser(user);
        entity.setMembershipPackage(membershipPackage);
        return entity;
    }
}