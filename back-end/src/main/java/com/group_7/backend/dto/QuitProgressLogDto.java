package com.group_7.backend.dto;

import java.time.LocalDate;

public class QuitProgressLogDto {
    private long logId;
    private long stageId;
    private LocalDate logDate;
    private Integer cigarettesSmoked;
    private String healthNote;
    private String notes;
}