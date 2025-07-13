package com.group_7.backend.service;

import com.group_7.backend.dto.ChatMessageDto;
import java.util.List;

public interface IChatService {
    ChatMessageDto saveMessage(ChatMessageDto message);
    List<ChatMessageDto> getChatHistory(Long userId1, Long userId2);
    void markMessagesAsRead(Long fromUserId, Long toUserId);
}