package com.group_7.backend.repository;

import com.group_7.backend.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByFromUserIdAndToUserIdOrderBySentAtAsc(Long fromUserId, Long toUserId);
    List<ChatMessage> findByToUserIdAndReadFalse(Long toUserId);
}
