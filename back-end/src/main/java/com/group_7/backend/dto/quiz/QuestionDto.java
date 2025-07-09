package com.group_7.backend.dto.quiz;

import com.group_7.backend.entity.enums.QuestionTypeEnum;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class QuestionDto {
    private Long id;
    private String questionText;
    private QuestionTypeEnum questionType;
    private Set<OptionDto> optionDtos;
    private String answerText;

    public QuestionDto(Long id, QuestionTypeEnum questionTypeEnum, String questionText, Set<OptionDto> optionDtos) {
        this.id = id;
        this.questionText = questionText;
        this.questionType = questionTypeEnum;
        this.optionDtos = optionDtos;
        this.answerText = null;
    }

    public QuestionDto(Long id, QuestionTypeEnum questionTypeEnum, String questionText, String answerText) {
        this.id = id;
        this.questionText = questionText;
        this.questionType = questionTypeEnum;
        this.optionDtos = null;
        this.answerText = answerText;
    }
}