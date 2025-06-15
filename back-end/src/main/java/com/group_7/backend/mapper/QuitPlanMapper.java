package com.group_7.backend.mapper;

import com.group_7.backend.dto.QuitPlanDto;
import com.group_7.backend.entity.QuitPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuitPlanMapper {
    QuitPlanDto toDto(QuitPlan entity);

    @Mapping(target = "createdAt", ignore = true)
    QuitPlan toEntity(QuitPlanDto dto);
}