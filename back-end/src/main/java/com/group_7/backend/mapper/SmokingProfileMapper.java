package com.group_7.backend.mapper;

import com.group_7.backend.dto.SmokingProfileDto;
import com.group_7.backend.entity.SmokingProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SmokingProfileMapper {
    @Mapping(target = "userId", source = "user.userId")
    SmokingProfileDto toDto(SmokingProfile entity);

    @Mapping(target = "user.userId", source = "userId")
    SmokingProfile toEntity(SmokingProfileDto dto);
}