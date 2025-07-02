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
    private Long logId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StageID", referencedColumnName = "StageID", nullable = false, unique = true)
    private QuitPlanStage quitPlanStage;

    @Column(name = "CreatedAt")
    private LocalDate createdAt = LocalDate.now();

    @Column(name = "CigarettesSmoked")
    private int cigarettesSmoked = 0;

    @Column(name = "Notes", columnDefinition = "NVARCHAR(MAX)")
    private String notes = null;

    public void setQuitPlanStage(QuitPlanStage quitPlanStage) {
        this.quitPlanStage = quitPlanStage;
    }
}