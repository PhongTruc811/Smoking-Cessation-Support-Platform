package com.group_7.backend.dto.request;

import com.group_7.backend.dto.quiz.QuestionDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserQuizAnswerRequestDto {
    @NotBlank
    private String quizId;
    @NotNull
    private Long userId;
    private List<QuestionDto> questions;
}
