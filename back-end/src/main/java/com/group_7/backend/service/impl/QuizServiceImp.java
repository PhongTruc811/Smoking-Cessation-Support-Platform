package com.group_7.backend.service.impl;

import com.group_7.backend.dto.quiz.*;
import com.group_7.backend.entity.quiz.*;
import com.group_7.backend.mapper.QuizMapper;
import com.group_7.backend.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class QuizServiceImp {
    @Autowired
    private QuizRepository quizRepository;

    public List<QuizDto> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();
        return QuizMapper.toDtoList(quizzes);
    }

    public QuizDto getQuizById(String id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        return quiz.map(QuizMapper::toDto).orElse(null);
    }

    @Transactional
    public QuizDto createQuiz(QuizDto quizDto) {
        Quiz quiz = QuizMapper.toEntity(quizDto);
        Quiz savedQuiz = quizRepository.save(quiz);
        return QuizMapper.toDto(savedQuiz);
    }

    @Transactional
    public QuizDto updateQuiz(String id, QuizDto quizDto) {
        if (!quizRepository.existsById(id)) return null;
        Quiz quiz = QuizMapper.toEntity(quizDto);
        Quiz updated = quizRepository.save(quiz);
        return QuizMapper.toDto(updated);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public boolean deleteQuiz(String id) {
        if (!quizRepository.existsById(id)) return false;
        quizRepository.deleteById(id);
        return true;
    }
}