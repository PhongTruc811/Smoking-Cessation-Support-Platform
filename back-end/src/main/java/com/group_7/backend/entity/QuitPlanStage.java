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
    private Long stageId;

    @Column(name = "StageNumber")
    private int stageNumber;

    @Column(name = "Description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "Duration")
    private int duration;

    @Column(name = "IsCompleted")
    private boolean isCompleted = false;

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