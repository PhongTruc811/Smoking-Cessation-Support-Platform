package com.group_7.backend.dto;

import com.group_7.backend.entity.enums.QuitPlanStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuitPlanDto {
    private Long id;

    @NotNull(message = "User ID must not be null")
    @Positive(message = "User ID must be a positive number")
    private Long userId;

    @NotBlank(message = "Reason must not be blank")
    @Size(min = 10, message = "Reason must be at least 10 characters")
    private String reason;

    @NotNull(message = "Start date must not be empty")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "Target end date must not be empty")
    @Future(message = "Target end date must be in the future")
    private LocalDate targetEndDate;

    private LocalDate createdAt;
    private QuitPlanStatusEnum status;

    @Min(value = 0, message = "Daily smoke count cannot be negative")
    private int dailySmoke;

    private int totalSmoke;
    private Set<QuitMethodOptionDto> methodOptions;
    private List<QuitProgressLogDto> progressLogs;

}