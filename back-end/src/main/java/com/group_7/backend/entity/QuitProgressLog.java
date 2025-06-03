package com.group_7.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "QuitProgressLogs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuitProgressLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LogID")
    private Integer logId;

    @OneToOne
    @JoinColumn(name = "StageID", referencedColumnName = "StageID", unique = true)
    private QuitPlanStage quitPlanStage;

    @Column(name = "LogDate")
    private LocalDate logDate;

    @Column(name = "CigarettesSmoked")
    private Integer cigarettesSmoked;

    @Column(name = "HealthNote")
    private String healthNote;

    @Column(name = "Notes")
    private String notes;
}