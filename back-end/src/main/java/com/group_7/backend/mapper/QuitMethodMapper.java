package com.group_7.backend.mapper;

import com.group_7.backend.dto.QuitMethodDto;
import com.group_7.backend.entity.QuitMethod;
import com.group_7.backend.entity.QuitMethodOption;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class QuitMethodMapper {

    private final QuitMethodOptionMapper quitMethodOptionMapper;

    public QuitMethodMapper(QuitMethodOptionMapper quitMethodOptionMapper) {
        this.quitMethodOptionMapper = quitMethodOptionMapper;
    }

    public QuitMethodDto toDto(QuitMethod entity) {
        if (entity == null) {
            return null;
        }

        QuitMethodDto dto = new QuitMethodDto();
        dto.setId(entity.getId());
        dto.setMethodName(entity.getMethodName());
        dto.setMethodType(entity.getMethodType());

        if (entity.getOptions() != null) {
            dto.setOptions(entity.getOptions().stream()
                    .map(quitMethodOptionMapper::toDto)
                    .collect(Collectors.toSet()));
        } else {
            dto.setOptions(Collections.emptySet());
        }

        return dto;
    }

    public QuitMethod toEntity(QuitMethodDto dto) {
        if (dto == null) {
            return null;
        }

        QuitMethod entity = new QuitMethod();
        // entity.setId(dto.getId()); // Không set ID nếu là tạo mới (JPA sẽ tự tạo). Chỉ set nếu bạn đang cập nhật.
        entity.setMethodName(dto.getMethodName());
        entity.setMethodType(dto.getMethodType());

        if (dto.getOptions() != null) {
            Set<QuitMethodOption> options = dto.getOptions().stream()
                    .map(quitMethodOptionMapper::toEntity)
                    .collect(Collectors.toSet());
            // Thiết lập mối quan hệ ngược lại cho One-to-Many
            options.forEach(option -> option.setQuitMethod(entity));
            entity.setOptions(options);
        } else {
            entity.setOptions(Collections.emptySet());
        }

        return entity;
    }

    public List<QuitMethodDto> toDtoList(List<QuitMethod> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<QuitMethod> toEntityList(List<QuitMethodDto> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public Set<QuitMethodDto> toDtoSet(Set<QuitMethod> entities) {
        if (entities == null) {
            return Collections.emptySet();
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    public Set<QuitMethod> toEntitySet(Set<QuitMethodDto> dtos) {
        if (dtos == null) {
            return Collections.emptySet();
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }
}