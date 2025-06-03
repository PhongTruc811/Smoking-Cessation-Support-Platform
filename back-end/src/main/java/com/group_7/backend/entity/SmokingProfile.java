package com.group_7.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "SmokingProfiles")
@AllArgsConstructor
@NoArgsConstructor
public class SmokingProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SmokingProfileID")
    private long smokingProfileId;

    @OneToOne
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", unique = true)
    private User user;

    @Column(name = "CigarettesPerDay")
    private Integer cigarettesPerDay;

    @Column(name = "CostPerPack")
    private Double costPerPack;

    @Column(name = "WeekSmoked")
    private Integer weekSmoked;

    @Column(name = "Note")
    private String note;
}
