package com.group_7.backend.mapper;

import com.group_7.backend.dto.FeedbackDto;
import com.group_7.backend.entity.Feedback;
import com.group_7.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {
    public FeedbackDto toDto(Feedback entity) {
        if (entity == null) {
            return null;
        }

        FeedbackDto dto = new FeedbackDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : null);
        dto.setUserName(entity.getUser() != null ? entity.getUser().getFullName() : null);
        dto.setReceiverId(entity.getReceiverId());
        dto.setSubject(entity.getSubject());
        dto.setBody(entity.getBody());
        dto.setRating(entity.getRating());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public Feedback toEntity(FeedbackDto dto, User user) {
        if (dto == null) {
            return null;
        }

        Feedback entity = new Feedback();
        entity.setUser(user);
        entity.setReceiverId(dto.getReceiverId());
        entity.setSubject(dto.getSubject());
        entity.setRating(dto.getRating());
        entity.setBody(dto.getBody());
        return entity;
    }
}