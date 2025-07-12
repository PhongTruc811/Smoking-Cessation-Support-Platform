package com.group_7.backend.mapper;

import com.group_7.backend.dto.QuitPlanDto;
import com.group_7.backend.dto.QuitProgressLogDto;
import com.group_7.backend.dto.QuitMethodOptionDto;
import com.group_7.backend.entity.QuitPlan;
import com.group_7.backend.entity.User;
import com.group_7.backend.entity.QuitProgressLog;
import com.group_7.backend.entity.QuitMethodOption;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class QuitPlanMapper {

    private final QuitProgressLogMapper quitProgressLogMapper;
    private final QuitMethodOptionMapper quitMethodOptionMapper;

    public QuitPlanMapper(QuitProgressLogMapper quitProgressLogMapper,
                          QuitMethodOptionMapper quitMethodOptionMapper) {
        this.quitProgressLogMapper = quitProgressLogMapper;
        this.quitMethodOptionMapper = quitMethodOptionMapper;
    }

    public QuitPlanDto toDto(QuitPlan entity) {
        if (entity == null) {
            return null;
        }

        QuitPlanDto dto = new QuitPlanDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : null);
        dto.setReason(entity.getReason());
        dto.setStartDate(entity.getStartDate());
        dto.setTargetEndDate(entity.getTargetEndDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setStatus(entity.getStatus());
        dto.setDailySmoke(entity.getDailySmoke());
        dto.setTotalSmoke(entity.getTotalSmoke());
        if (entity.getMethodOptions() != null) {
            dto.setMethodOptions(
                    entity.getMethodOptions().stream()
                            .map(quitMethodOptionMapper::toDto)
                            .collect(Collectors.toSet())
            );
        } else {
            dto.setMethodOptions(Collections.emptySet());
        }

        if (entity.getProgressLogs() != null) {
            dto.setProgressLogs(
                    entity.getProgressLogs().stream()
                            .map(quitProgressLogMapper::toDto)
                            .collect(Collectors.toList())
            );
        } else {
            dto.setProgressLogs(Collections.emptyList());
        }

        return dto;
    }

    public QuitPlan toEntity(QuitPlanDto dto, User user) {
        if (dto == null) {
            return null;
        }

        QuitPlan entity = new QuitPlan();
        entity.setUser(user);
        entity.setReason(dto.getReason());
        entity.setStartDate(dto.getStartDate());
        entity.setTargetEndDate(dto.getTargetEndDate());
        entity.setStatus(dto.getStatus());
        entity.setDailySmoke(dto.getDailySmoke());

        return entity;
    }
}