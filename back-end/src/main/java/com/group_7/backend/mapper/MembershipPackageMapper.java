package com.group_7.backend.mapper;

import com.group_7.backend.dto.MembershipPackageDto;
import com.group_7.backend.entity.MembershipPackage;
import org.springframework.stereotype.Component;

@Component
public class MembershipPackageMapper {
    // Entity -> DTO
    public MembershipPackageDto toDto(MembershipPackage entity) {
        if (entity == null) return null;
        MembershipPackageDto dto = new MembershipPackageDto();
        dto.setId(entity.getId());
        dto.setPackageName(entity.getPackageName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setDurationInDays(entity.getDurationInDays());
        dto.setActive(entity.isActive());
        return dto;
    }

    // DTO -> Entity
    public MembershipPackage toEntity(MembershipPackageDto dto) {
        if (dto == null) return null;
        MembershipPackage entity = new MembershipPackage();
        entity.setPackageName(dto.getPackageName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setDurationInDays(dto.getDurationInDays());
        return entity;
    }
}