package com.group_7.backend.dto;

import com.group_7.backend.entity.enums.QuitPlanStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuitPlanDto {
    private long id;

    @Positive(message = "User ID must be a positive number")
    private long userId;

    @NotBlank(message = "Reason must not be blank")
    @Size(min = 10, message = "Reason must be at least 10 characters")
    private String reason;

    @NotNull(message = "Start date must not be empty")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "Target end date must not be empty")
    @Future(message = "Target end date must be in the future")
    private LocalDate targetEndDate;

    private LocalDateTime createdAt;

    @NotNull(message = "Status must not be empty")
    private QuitPlanStatusEnum status;

    @Valid
    @NotEmpty(message = "Quit plan must have at least one stage")
    private List<QuitPlanStageDto> quitPlanStages;

    // TODO: Add custom validator to ensure that targetEndDate > startDate
}