package com.group_7.backend.mapper;

import com.group_7.backend.dto.QuitMethodOptionDto;
import com.group_7.backend.entity.QuitMethodOption;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class QuitMethodOptionMapper {

    public QuitMethodOptionDto toDto(QuitMethodOption entity) {
        if (entity == null) {
            return null;
        }

        QuitMethodOptionDto dto = new QuitMethodOptionDto();
        dto.setId(entity.getId());
        dto.setOptionText(entity.getOptionText());
        dto.setOptionDescription(entity.getOptionDescription());
        dto.setOptionNoti(entity.getOptionNoti());

        return dto;
    }

    public QuitMethodOption toEntity(QuitMethodOptionDto dto) {
        if (dto == null) {
            return null;
        }

        QuitMethodOption entity = new QuitMethodOption();

        entity.setOptionText(dto.getOptionText());
        entity.setOptionDescription(dto.getOptionDescription());
        entity.setOptionNoti(dto.getOptionNoti());
        return entity;
    }
}