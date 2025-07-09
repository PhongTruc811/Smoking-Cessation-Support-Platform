package com.group_7.backend.controller;

import com.group_7.backend.dto.quiz.QuizDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.impl.QuizServiceImp;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizServiceImp quizService;

    public QuizController(QuizServiceImp quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllQuizzes() {
        return ResponseEntity.ok(
                new ResponseDto("success", "All quizzes fetched successfully", quizService.getAllQuizzes())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getQuizById(@PathVariable String id) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Quiz fetched successfully", quizService.getQuizById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createQuiz(@Valid @RequestBody QuizDto quizDto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Quiz created successfully", quizService.createQuiz(quizDto))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateQuiz(@PathVariable String id, @Valid @RequestBody QuizDto quizDto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Quiz updated successfully", quizService.updateQuiz(id, quizDto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteQuiz(@PathVariable String id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok(
                new ResponseDto("success", "Quiz deleted successfully", null)
        );
    }
}