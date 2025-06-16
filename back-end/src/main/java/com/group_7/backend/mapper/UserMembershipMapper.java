package com.group_7.backend.mapper;

import com.group_7.backend.dto.UserMembershipDto;
import com.group_7.backend.entity.UserMembership;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMembershipMapper {
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "membershipPackageId", source = "membershipPackage.id")
    UserMembershipDto toDto(UserMembership entity);

    UserMembership toEntity(UserMembershipDto dto);
}