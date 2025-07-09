package com.group_7.backend.mapper;

import com.group_7.backend.dto.SmokingProfileDto;
import com.group_7.backend.entity.SmokingProfile;
import com.group_7.backend.entity.User;
import com.group_7.backend.entity.enums.NicotineAddictionEnum; // Import if needed for manual mapping
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.math.BigDecimal; // Import BigDecimal

@Component
public class SmokingProfileMapper {

    /**
     * Converts a SmokingProfile entity to a SmokingProfileDto.
     * Used for sending data from Backend to Frontend (Response DTO).
     *
     * @param entity The SmokingProfile entity.
     * @return The converted SmokingProfileDto.
     */
    public SmokingProfileDto toDto(SmokingProfile entity) {
        if (entity == null) {
            return null;
        }
        SmokingProfileDto dto = new SmokingProfileDto();
        dto.setSmokingProfileId(entity.getSmokingProfileId());
        // Lấy userId từ User entity liên kết, nếu User là null thì userId của DTO cũng là null
        dto.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : null);
        dto.setCigarettesPerDay(entity.getCigarettesPerDay());
        dto.setCostPerPack(entity.getCostPerPack());
        dto.setWeekSmoked(entity.getWeekSmoked());
        dto.setNote(entity.getNote());

        // Ánh xạ các trường tính toán và ngày tháng
        dto.setNicotineAddiction(entity.getNicotineAddiction());
        dto.setFtndScore(entity.getFtndScore());
        dto.setCreateAt(entity.getCreateAt());
        dto.setLastUpdateDate(entity.getLastUpdateDate());

        return dto;
    }

    /**
     * Converts a SmokingProfileDto to a new SmokingProfile entity for creation.
     * This method is typically used when you're creating a NEW SmokingProfile.
     * userId is handled by linking the User entity in the service layer.
     *
     * @param dto The SmokingProfileDto from Frontend.
     * @return A new SmokingProfile entity.
     */
    public SmokingProfile toEntity(SmokingProfileDto dto, User user) {
        if (dto == null) {
            return null;
        }

        SmokingProfile entity = new SmokingProfile();

        entity.setUser(user);
        entity.setCigarettesPerDay(dto.getCigarettesPerDay());
        entity.setCostPerPack(dto.getCostPerPack());
        entity.setWeekSmoked(dto.getWeekSmoked());
        entity.setNote(dto.getNote());
        entity.setFtndScore(dto.getFtndScore());
        return entity;
    }
}