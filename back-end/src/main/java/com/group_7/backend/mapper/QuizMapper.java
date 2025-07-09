package com.group_7.backend.mapper;

import com.group_7.backend.dto.quiz.*;
import com.group_7.backend.entity.quiz.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class QuizMapper {

    // ENTITY -> DTO
    public static QuizDto toDto(Quiz quiz) {
        if (quiz == null) return null;
        QuizDto dto = new QuizDto();
        dto.setQuizId(quiz.getQuizId());
        dto.setName(quiz.getName());
        dto.setDescription(quiz.getDescription());
        if (quiz.getQuestions() != null)
            dto.setQuestions(quiz.getQuestions().stream().map(QuizMapper::toDto).collect(Collectors.toList()));
        return dto;
    }

    public static QuestionDto toDto(Question question) {
        if (question == null) return null;
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setQuestionText(question.getQuestionText());
        dto.setQuestionType(question.getQuestionTypeEnum());
        dto.setAnswerText(question.getAnswer());
        if (question.getOptions() != null)
            dto.setOptionDtos(question.getOptions().stream().map(QuizMapper::toDto).collect(Collectors.toSet()));
        return dto;
    }

    public static OptionDto toDto(Option option) {
        if (option == null) return null;
        OptionDto dto = new OptionDto();
        dto.setId(option.getId());
        dto.setOptionText(option.getOptionText());
        dto.setScore(option.getScore());
        return dto;
    }

    // DTO -> ENTITY
    public static Quiz toEntity(QuizDto dto) {
        if (dto == null) return null;
        Quiz quiz = new Quiz();
        quiz.setQuizId(dto.getQuizId());
        quiz.setName(dto.getName());
        quiz.setDescription(dto.getDescription());
        if (dto.getQuestions() != null) {
            List<Question> questions = dto.getQuestions().stream()
                    .map(QuizMapper::toEntity)
                    .collect(Collectors.toList());
            // Gán lại Quiz reference cho mỗi question
            questions.forEach(q -> q.setQuiz(quiz));
            quiz.setQuestions(questions);
        }
        return quiz;
    }

    public static Question toEntity(QuestionDto dto) {
        if (dto == null) return null;
        Question question = new Question();
        question.setId(dto.getId());
        question.setQuestionText(dto.getQuestionText());
        question.setQuestionTypeEnum(dto.getQuestionType());
        question.setAnswer(dto.getAnswerText());
        if (dto.getOptionDtos() != null) {
            Set<Option> options = dto.getOptionDtos().stream()
                    .map(QuizMapper::toEntity)
                    .collect(Collectors.toSet());
            // Gán lại Question reference cho mỗi option
            options.forEach(opt -> opt.setQuestion(question));
            question.setOptions(options);
        }
        return question;
    }

    public static Option toEntity(OptionDto dto) {
        if (dto == null) return null;
        Option option = new Option();
        option.setId(dto.getId());
        option.setOptionText(dto.getOptionText());
        option.setScore(dto.getScore());
        return option;
    }

    // List utilities
    public static List<QuizDto> toDtoList(List<Quiz> quizzes) {
        if (quizzes == null) return null;
        return quizzes.stream().map(QuizMapper::toDto).collect(Collectors.toList());
    }

    public static List<Quiz> toEntityList(List<QuizDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(QuizMapper::toEntity).collect(Collectors.toList());
    }
}