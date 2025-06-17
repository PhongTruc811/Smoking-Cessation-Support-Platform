package com.group_7.backend.mapper;

import com.group_7.backend.dto.SmokingProfileDto;
import com.group_7.backend.entity.SmokingProfile;
import com.group_7.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class SmokingProfileMapper {

    // Entity -> DTO
    public SmokingProfileDto toDto(SmokingProfile entity) {
        if (entity == null) return null;
        SmokingProfileDto dto = new SmokingProfileDto();
        dto.setSmokingProfileId(entity.getSmokingProfileId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : 0);
        dto.setCigarettesPerDay(entity.getCigarettesPerDay());
        dto.setCostPerPack(entity.getCostPerPack());
        dto.setWeekSmoked(entity.getWeekSmoked());
        dto.setNote(entity.getNote());
        return dto;
    }

    // DTO -> Entity (user cần truyền vào hoặc set sau, vì chỉ có userId trong dto)
    public SmokingProfile toEntity(SmokingProfileDto dto, User user) {
        if (dto == null) return null;
        SmokingProfile entity = new SmokingProfile();
        entity.setUser(user);
        entity.setCigarettesPerDay(dto.getCigarettesPerDay());
        entity.setCostPerPack(dto.getCostPerPack());
        entity.setWeekSmoked(dto.getWeekSmoked());
        entity.setNote(dto.getNote());
        return entity;
    }
}