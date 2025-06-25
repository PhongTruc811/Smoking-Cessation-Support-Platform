package com.group_7.backend.mapper;

import com.group_7.backend.dto.QuitProgressLogDto;
import com.group_7.backend.entity.QuitProgressLog;
import com.group_7.backend.entity.QuitPlanStage;
import org.springframework.stereotype.Component;

@Component
public class QuitProgressLogMapper {

    // Entity -> DTO
    public QuitProgressLogDto toDto(QuitProgressLog entity) {
        if (entity == null) return null;
        QuitProgressLogDto dto = new QuitProgressLogDto();
        dto.setLogId(entity.getLogId());
        dto.setStageId(entity.getQuitPlanStage() != null ? entity.getQuitPlanStage().getStageId() : 0);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCigarettesSmoked(entity.getCigarettesSmoked());
        dto.setNotes(entity.getNotes());
        return dto;
    }

    // DTO -> Entity (cần truyền QuitPlanStage)
    public QuitProgressLog toEntity(QuitProgressLogDto dto, QuitPlanStage quitPlanStage) {
        if (dto == null) return null;
        QuitProgressLog entity = new QuitProgressLog();
        entity.setLogId(dto.getLogId());
        entity.setQuitPlanStage(quitPlanStage);
        entity.setCigarettesSmoked(dto.getCigarettesSmoked());
        entity.setNotes(dto.getNotes());
        return entity;
    }
}