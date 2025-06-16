package com.group_7.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "QuitPlanStages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuitPlanStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StageID")
    private long stageId;

    @Column(name = "StageName", length = 100)
    private String stageName;

    @Column(name = "Description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @Column(name = "IsCompleted")
    private Boolean isCompleted = false;

    @OneToOne(mappedBy = "quitPlanStage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private QuitProgressLog quitProgressLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QuitPlanId",nullable = false)
    private QuitPlan quitPlan;

    //PlanStage này gắn với ProgressLog nào
    public void setQuitProgressLog(QuitProgressLog quitProgressLog) {
        quitProgressLog.setQuitPlanStage(this);
        this.quitProgressLog = quitProgressLog;
    }
}