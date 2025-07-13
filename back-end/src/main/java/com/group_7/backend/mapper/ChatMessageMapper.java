package com.group_7.backend.mapper;

import com.group_7.backend.dto.ChatMessageDto;
import com.group_7.backend.entity.ChatMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ChatMessageMapper {
    public ChatMessage toEntity(ChatMessageDto dto) {
        ChatMessage entity = new ChatMessage();
        entity.setFromUserId(dto.getFromUserId());
        entity.setToUserId(dto.getToUserId());
        entity.setContent(dto.getContent());
        // If you want to allow setting timestamp from DTO, parse it here; otherwise, use now()
        entity.setSentAt(dto.getTimestamp() != null ? LocalDateTime.parse(dto.getTimestamp()) : LocalDateTime.now());
        entity.setRead(false);
        return entity;
    }

    public ChatMessageDto toDto(ChatMessage entity) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setFromUserId(entity.getFromUserId());
        dto.setToUserId(entity.getToUserId());
        dto.setContent(entity.getContent());
        dto.setTimestamp(entity.getSentAt().toString());
        return dto;
    }
}