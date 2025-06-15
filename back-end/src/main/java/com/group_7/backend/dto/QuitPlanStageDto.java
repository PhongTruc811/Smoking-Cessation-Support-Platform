package com.group_7.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class QuitPlanStageDto {
    private long stageId;

    @NotBlank(message = "Stage name must not be blank")
    @Size(max = 100, message = "Stage name must not exceed 100 characters")
    private String stageName;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Stage start date must not be null")
    private LocalDate startDate;

    @NotNull(message = "Stage end date must not be null")
    private LocalDate endDate;

    @NotNull(message = "Completion status must not be null")
    private Boolean isCompleted;

    @Valid
    private QuitProgressLogDto quitProgressLog;

    // TODO: Add custom validator to ensure endDate >= startDate
}