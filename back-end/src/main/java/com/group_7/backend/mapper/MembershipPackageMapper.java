package com.group_7.backend.mapper;

import com.group_7.backend.dto.MembershipPackageDto;
import com.group_7.backend.entity.MembershipPackage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MembershipPackageMapper {
    MembershipPackageDto toDTO(MembershipPackage entity);
    MembershipPackage toEntity(MembershipPackageDto dto);
}