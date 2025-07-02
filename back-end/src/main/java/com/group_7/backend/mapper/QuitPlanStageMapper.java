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
        dto.setStageNumber(entity.getStageNumber());
        dto.setDescription(entity.getDescription());
        dto.setStartDate(entity.getStartDate());
        dto.setDuration(entity.getDuration());
        dto.setCompleted(entity.isCompleted());
        return dto;
    }

    // DTO -> Entity (cần truyền QuitPlan, log nếu có sẽ map riêng)
    public QuitPlanStage toEntity(QuitPlanStageDto dto, QuitPlan quitPlan) {
        if (dto == null) return null;
        QuitPlanStage entity = new QuitPlanStage();
        entity.setStageNumber(dto.getStageNumber());
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setDuration(dto.getDuration());
        entity.setQuitPlan(quitPlan);
        // quitProgressLog sẽ được set sau nếu cần, hoặc:
        return entity;
    }
}