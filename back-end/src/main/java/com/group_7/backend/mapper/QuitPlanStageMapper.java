package com.group_7.backend.mapper;

import com.group_7.backend.dto.QuitPlanStageDto;
import com.group_7.backend.entity.QuitPlanStage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuitPlanStageMapper {
    QuitPlanStageDto toDTO(QuitPlanStage entity);
    QuitPlanStage toEntity(QuitPlanStageDto dto);
}