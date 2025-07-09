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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PlanID", nullable = false)
    private QuitPlan quitPlan;

    @Column(name = "CreatedAt")
    private LocalDate createdAt = LocalDate.now();

    @Column(name = "CigarettesSmoked")
    private int cigarettesSmoked = 0;

    @Column(name = "Notes", columnDefinition = "NVARCHAR(MAX)")
    private String notes = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuitProgressLog that = (QuitProgressLog) o;
        return logId != null && logId.equals(that.logId);
    }

    @Override
    public int hashCode() {
        return logId != null ? logId.hashCode() : 0;
    }
}