package com.group_7.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmokingProfileDto {
    private long smokingProfileId;
    private long userId;
    private Integer cigarettesPerDay;
    private Double costPerPack;
    private Integer weekSmoked;
    private String note;
}