package com.group_7.backend.mapper;

import com.group_7.backend.dto.QuitPlanStageDto;
import com.group_7.backend.entity.QuitPlanStage;
import com.group_7.backend.entity.QuitPlan;
import org.springframework.stereotype.Component;

@Component
public class QuitPlanStageMapper {

    // Entity -> DTO
    public QuitPlanStageDto toDto(QuitPlanStage entity) {
        if (entity == null) return null;
        QuitPlanStageDto dto = new QuitPlanStageDto();
        dto.setStageId(entity.getStageId());
        dto.setStageName(entity.getStageName());
        dto.setDescription(entity.getDescription());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setIsCompleted(entity.getIsCompleted());
        return dto;
    }

    // DTO -> Entity (cần truyền QuitPlan, log nếu có sẽ map riêng)
    public QuitPlanStage toEntity(QuitPlanStageDto dto, QuitPlan quitPlan) {
        if (dto == null) return null;
        QuitPlanStage entity = new QuitPlanStage();
        entity.setStageId(dto.getStageId());
        entity.setStageName(dto.getStageName());
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setIsCompleted(dto.getIsCompleted());
        entity.setQuitPlan(quitPlan);
        // quitProgressLog sẽ được set sau nếu cần, hoặc:
        return entity;
    }
}