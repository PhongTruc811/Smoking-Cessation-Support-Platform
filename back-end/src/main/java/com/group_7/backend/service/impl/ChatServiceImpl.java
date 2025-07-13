package com.group_7.backend.service.impl;

import com.group_7.backend.dto.ChatMessageDto;
import com.group_7.backend.entity.ChatMessage;
import com.group_7.backend.mapper.ChatMessageMapper;
import com.group_7.backend.repository.ChatMessageRepository;
import com.group_7.backend.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements IChatService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Override
    public ChatMessageDto saveMessage(ChatMessageDto message) {
        ChatMessage entity = chatMessageMapper.toEntity(message);
        ChatMessage saved = chatMessageRepository.save(entity);
        return chatMessageMapper.toDto(saved);
    }

    @Override
    public List<ChatMessageDto> getChatHistory(Long userId1, Long userId2) {
        List<ChatMessage> messages = chatMessageRepository
            .findByFromUserIdAndToUserIdOrderBySentAtAsc(userId1, userId2);
        messages.addAll(chatMessageRepository
            .findByFromUserIdAndToUserIdOrderBySentAtAsc(userId2, userId1));
        messages.sort(Comparator.comparing(ChatMessage::getSentAt));
        return messages.stream().map(chatMessageMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void markMessagesAsRead(Long fromUserId, Long toUserId) {
        List<ChatMessage> unread = chatMessageRepository
            .findByFromUserIdAndToUserIdOrderBySentAtAsc(fromUserId, toUserId)
            .stream().filter(m -> !m.isRead()).collect(Collectors.toList());
        unread.forEach(m -> m.setRead(true));
        chatMessageRepository.saveAll(unread);
    }
}
