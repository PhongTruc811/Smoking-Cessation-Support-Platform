package com.group_7.backend.dto.quiz;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {
    private String quizId;
    private String name;
    private String description;
    private List<QuestionDto> questions;
}