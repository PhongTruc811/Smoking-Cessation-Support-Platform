package com.group_7.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "SmokingProfiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmokingProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SmokingProfileID")
    private Long smokingProfileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", unique = true)
    private User user;

    @Column(name = "CigarettesPerDay")
    private int cigarettesPerDay;

    @Column(name = "CostPerPack")
    private BigDecimal costPerPack;

    @Column(name = "WeekSmoked")
    private int weekSmoked;

    @Column(name = "Note", columnDefinition = "NVARCHAR(MAX)")
    private String note;

    //SmokingProfile thuộc USER này và ngược lại
    public void setUser(User user) {
        this.user = user;
        user.setSmokingProfile(this);
    }
}
