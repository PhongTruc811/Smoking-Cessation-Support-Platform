package com.group_7.backend.dto;

import com.group_7.backend.entity.enums.QuestionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuitMethodDto {
    private Long id;
    private String methodName;
    private QuestionTypeEnum methodType;
    private Set<QuitMethodOptionDto> options; // Danh sách các options thuộc về method này
    private Long quitMethodId;
}