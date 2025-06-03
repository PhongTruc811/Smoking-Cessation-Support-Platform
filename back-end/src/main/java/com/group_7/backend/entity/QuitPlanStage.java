package com.group_7.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "QuitPlanStages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuitPlanStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StageID")
    private Integer stageId;

    @Column(name = "PlanID")
    private Integer planId;

    @Column(name = "StageName", length = 100)
    private String stageName;

    @Column(name = "Description", length = 255)
    private String description;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @Column(name = "IsCompleted")
    private Boolean isCompleted = false;

    @OneToOne(mappedBy = "quitPlanStage", cascade = CascadeType.ALL)
    private QuitProgressLog quitProgressLog;
}