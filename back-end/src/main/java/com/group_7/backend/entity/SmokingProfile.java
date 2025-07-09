package com.group_7.backend.entity;

import com.group_7.backend.entity.enums.NicotineAddictionEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Column(name = "CigarettesPerDay", columnDefinition = "NVARCHAR(100)")
    private String cigarettesPerDay;

    @Column(name = "CostPerPack")
    private BigDecimal costPerPack;

    @Column(name = "WeekSmoked")
    private int weekSmoked;

    @Column(name = "Note", columnDefinition = "NVARCHAR(MAX)")
    private String note;

    @Column(name = "NicotineAddiction")
    private NicotineAddictionEnum nicotineAddiction;

    @Column(name = "FTNDScore")
    private int ftndScore;

    @Column(name = "CreateAt")
    private LocalDate createAt = LocalDate.now();

    @Column(name = "LastUpdateDate")
    private LocalDate lastUpdateDate = LocalDate.now();

    //SmokingProfile thuộc USER này và ngược lại
    public void setUser(User user) {
        this.user = user;
        user.setSmokingProfile(this);
    }

    public void setFtndScore(int ftndScore) {
        this.ftndScore = ftndScore;
        switch (ftndScore) {
            case 0,1,2,3:
                this.nicotineAddiction = NicotineAddictionEnum.LOW;
                break;
            case 4,5,6,7:
                this.nicotineAddiction = NicotineAddictionEnum.MEDIUM;
                break;
            case 8,9,10:
                this.nicotineAddiction = NicotineAddictionEnum.HIGH;
                break;
        }
    }
}
