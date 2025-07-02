package com.group_7.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserRecordID")
    private Long userRecordId;

    @OneToOne
    @JoinColumn(name = "UserId", referencedColumnName = "UserID")
    private User user;

    private int totalQuitDays;
    private int totalQuitSmokes;
    private BigDecimal totalSaveMoney;
}