package com.group_7.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuitPlanStageDto {
    private Long stageId;

    @NotNull(message = "Stage number must not be null")
    @Min(value = 1, message = "StageNumber must be at least 1 ")
    @Max(value = 10, message = "StageNumber must be at most 10")
    private int stageNumber;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private LocalDate startDate;

    @NotNull(message = "Duration must not be null")
    @Min(value = 1, message = "Duration must be at least 1 day")
    @Max(value = 1000, message = "Duration must be at most 1000 days")
    private int duration;

    private boolean isCompleted;


    private QuitProgressLogDto quitProgressLog;

    // TODO: Add custom validator to ensure endDate >= startDate
}