package com.group_7.backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuitMethodOptionDto {
    private Long id;
    private String optionText;
    private String optionDescription;
    private String optionNoti;
}