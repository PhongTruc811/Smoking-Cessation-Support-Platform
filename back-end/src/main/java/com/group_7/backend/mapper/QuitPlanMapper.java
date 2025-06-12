package com.group_7.backend.mapper;

import com.group_7.backend.dto.QuitPlanDto;
import com.group_7.backend.entity.QuitPlan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuitPlanMapper {
    QuitPlanDto toDTO(QuitPlan entity);
    QuitPlan toEntity(QuitPlanDto dto);
}