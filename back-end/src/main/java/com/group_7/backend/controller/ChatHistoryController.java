package com.group_7.backend.controller;

import com.group_7.backend.dto.ChatMessageDto;
import com.group_7.backend.service.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController {
    @Autowired
    private IChatService chatService;

    @GetMapping("/history")
    public ResponseEntity<List<ChatMessageDto>> getHistory(
            @RequestParam Long userId1,
            @RequestParam Long userId2) {
        return ResponseEntity.ok(chatService.getChatHistory(userId1, userId2));
    }
}