package com.group_7.backend.mapper;

import com.group_7.backend.dto.QuitProgressLogDto;
import com.group_7.backend.entity.QuitProgressLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuitProgressLogMapper {
    QuitProgressLogDto toDto(QuitProgressLog entity);
    QuitProgressLog toEntity(QuitProgressLogDto dto);
}