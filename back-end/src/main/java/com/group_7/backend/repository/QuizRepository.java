package com.group_7.backend.repository;

import com.group_7.backend.entity.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, String> {
    // Có thể bổ sung các phương thức custom nếu cần
}