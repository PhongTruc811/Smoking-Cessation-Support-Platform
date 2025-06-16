package com.group_7.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "QuitProgressLogs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuitProgressLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LogID")
    private long logId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StageID", referencedColumnName = "StageID", nullable = false, unique = true)
    private QuitPlanStage quitPlanStage;

    @Column(name = "LogDate")
    private LocalDate logDate;

    @Column(name = "CigarettesSmoked")
    private Integer cigarettesSmoked;

    @Column(name = "HealthNote", columnDefinition = "NVARCHAR(MAX)")
    private String healthNote;

    @Column(name = "Notes", columnDefinition = "NVARCHAR(MAX)")
    private String notes;

    public void setQuitPlanStage(QuitPlanStage quitPlanStage) {
        this.quitPlanStage = quitPlanStage;
    }
}