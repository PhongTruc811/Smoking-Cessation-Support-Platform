package com.group_7.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private long smokingProfileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", unique = true)
    private User user;

    @Column(name = "CigarettesPerDay")
    private Integer cigarettesPerDay;

    @Column(name = "CostPerPack")
    private Double costPerPack;

    @Column(name = "WeekSmoked")
    private Integer weekSmoked;

    @Column(name = "Note", columnDefinition = "NVARCHAR(MAX)")
    private String note;

    //SmokingProfile thuộc USER này và ngược lại
    public void setUser(User user) {
        this.user = user;
        user.setSmokingProfile(this);
    }
}
