package com.group_7.backend.mapper;

import com.group_7.backend.dto.QuitProgressLogDto;
import com.group_7.backend.entity.QuitProgressLog;
import org.springframework.stereotype.Component;

@Component
public class QuitProgressLogMapper {

    // Entity -> DTO
    public QuitProgressLogDto toDto(QuitProgressLog entity) {
        if (entity == null) return null;
        QuitProgressLogDto dto = new QuitProgressLogDto();
        dto.setLogId(entity.getLogId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCigarettesSmoked(entity.getCigarettesSmoked());
        dto.setNotes(entity.getNotes());
        return dto;
    }

    // DTO -> Entity (cần truyền QuitPlanStage)
    public QuitProgressLog toEntity(QuitProgressLogDto dto) {
        if (dto == null) return null;
        QuitProgressLog entity = new QuitProgressLog();
        entity.setLogId(dto.getLogId());
        entity.setCigarettesSmoked(dto.getCigarettesSmoked());
        entity.setNotes(dto.getNotes());
        return entity;
    }
}