package com.group_7.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class QuitPlanDto {
    private long id;
    private long userId;
    private String reason;
    private LocalDate startDate;
    private LocalDate targetEndDate;
    private LocalDateTime createdAt;
    private String status;
    private List<QuitPlanStageDto> quitPlanStages;
}