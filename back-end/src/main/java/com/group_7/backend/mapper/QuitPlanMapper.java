package com.group_7.backend.mapper;

import com.group_7.backend.dto.QuitPlanDto;
import com.group_7.backend.entity.QuitPlan;
import com.group_7.backend.entity.QuitPlanStage;
import com.group_7.backend.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class QuitPlanMapper {

    private final QuitPlanStageMapper quitPlanStageMapper;

    public QuitPlanMapper(QuitPlanStageMapper quitPlanStageMapper) {
        this.quitPlanStageMapper = quitPlanStageMapper;
    }

    // Entity -> DTO
    public QuitPlanDto toDto(QuitPlan entity) {
        if (entity == null) return null;
        QuitPlanDto dto = new QuitPlanDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : 0);
        dto.setReason(entity.getReason());
        dto.setStartDate(entity.getStartDate());
        dto.setTargetEndDate(entity.getTargetEndDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setStatus(entity.getStatus());
        if (entity.getQuitPlanStages() != null) {
            dto.setQuitPlanStages(
                    entity.getQuitPlanStages()
                            .stream()
                            .map(quitPlanStageMapper::toDto)
                            .collect(Collectors.toSet())
            );
        }
        return dto;
    }

    // DTO -> Entity
    public QuitPlan toEntity(QuitPlanDto dto, User user) {
        if (dto == null) return null;
        QuitPlan entity = new QuitPlan();
        entity.setUser(user);
        entity.setReason(dto.getReason());
        entity.setStartDate(dto.getStartDate());
        entity.setTargetEndDate(dto.getTargetEndDate());
        if (dto.getQuitPlanStages() != null) {
            Set<QuitPlanStage> stages = dto.getQuitPlanStages().stream()
                    .map(stageDto -> quitPlanStageMapper.toEntity(stageDto, entity))
                    .collect(Collectors.toSet());
            entity.setQuitPlanStages(stages);
        }
        return entity;
    }
}