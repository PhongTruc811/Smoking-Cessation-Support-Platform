package com.group_7.backend.controller;

import com.group_7.backend.dto.ChatMessageDto;
import com.group_7.backend.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private IChatService chatService;

    // Handle incoming chat messages, persist, and broadcast to recipient
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageDto chatMessageDto) {
        ChatMessageDto saved = chatService.saveMessage(chatMessageDto);
        // Send to the recipient's topic
        messagingTemplate.convertAndSend("/topic/chat." + saved.getToUserId(), saved);
        // Optionally, also send to sender for echo
        messagingTemplate.convertAndSend("/topic/chat." + saved.getFromUserId(), saved);
    }

    // Typing indicator (broadcast, not persisted)
    @MessageMapping("/chat.typing")
    public void typing(@Payload TypingIndicator indicator) {
        // TypingIndicator is a simple DTO: fromUserId, toUserId, typing (boolean)
        messagingTemplate.convertAndSend("/topic/chat." + indicator.getToUserId(), indicator);
    }

    // Read receipt (update DB and notify sender)
    @MessageMapping("/chat.read")
    public void read(@Payload ReadReceipt receipt) {
        // ReadReceipt: fromUserId, toUserId
        chatService.markMessagesAsRead(receipt.getFromUserId(), receipt.getToUserId());
        messagingTemplate.convertAndSend("/topic/chat." + receipt.getFromUserId(), receipt);
    }

    // --- DTOs for typing and read ---
    public static class TypingIndicator {
        private Long fromUserId;
        private Long toUserId;
        private boolean typing;
        // getters/setters
        public Long getFromUserId() { return fromUserId; }
        public void setFromUserId(Long fromUserId) { this.fromUserId = fromUserId; }
        public Long getToUserId() { return toUserId; }
        public void setToUserId(Long toUserId) { this.toUserId = toUserId; }
        public boolean isTyping() { return typing; }
        public void setTyping(boolean typing) { this.typing = typing; }
    }

    public static class ReadReceipt {
        private Long fromUserId;
        private Long toUserId;
        // getters/setters
        public Long getFromUserId() { return fromUserId; }
        public void setFromUserId(Long fromUserId) { this.fromUserId = fromUserId; }
        public Long getToUserId() { return toUserId; }
        public void setToUserId(Long toUserId) { this.toUserId = toUserId; }
    }
}