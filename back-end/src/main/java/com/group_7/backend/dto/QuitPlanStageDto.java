package com.group_7.backend.dto;

import java.time.LocalDate;

public class QuitPlanStageDto {
    private long stageId;
    private String stageName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCompleted;
    private QuitProgressLogDto quitProgressLog;
}