package com.group_7.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private Long fromUserId;
    private Long toUserId;
    private String content;
    private String timestamp; // ISO 8601 string or you can use LocalDateTime if preferred
}
