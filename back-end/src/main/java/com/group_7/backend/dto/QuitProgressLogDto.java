package com.group_7.backend.dto;

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
public class QuitProgressLogDto {
    private long logId;

    @NotNull(message = "Stage ID must not be empty")
    @Positive(message = "Stage ID must be a positive number")
    private long stageId;

    private LocalDate createdAt;

    @NotNull(message = "Cigarettes smoked must not be empty")
    @PositiveOrZero(message = "Cigarettes smoked must be zero or a positive number")
    private Integer cigarettesSmoked;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}