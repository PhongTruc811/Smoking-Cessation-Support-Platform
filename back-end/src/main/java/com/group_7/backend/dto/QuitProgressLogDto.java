package com.group_7.backend.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class QuitProgressLogDto {
    private long logId;

    @NotNull(message = "Stage ID must not be empty")
    @Positive(message = "Stage ID must be a positive number")
    private long stageId;

    @NotNull(message = "Log date must not be empty")
    @PastOrPresent(message = "Log date must be today or in the past")
    private LocalDate logDate;

    @NotNull(message = "Cigarettes smoked must not be empty")
    @PositiveOrZero(message = "Cigarettes smoked must be zero or a positive number")
    private Integer cigarettesSmoked;

    @Size(max = 500, message = "Health note must not exceed 500 characters")
    private String healthNote;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}