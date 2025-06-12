package com.group_7.backend.mapper;

import com.group_7.backend.dto.UserMembershipDto;
import com.group_7.backend.entity.UserMembership;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMembershipMapper {
    UserMembershipDto toDTO(UserMembership entity);
    UserMembership toEntity(UserMembershipDto dto);
}