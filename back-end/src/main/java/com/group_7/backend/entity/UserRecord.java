package com.group_7.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserRecordID")
    private long userRecordId;

    @OneToOne
    @JoinColumn(name = "UserId", referencedColumnName = "UserID")
    private User user;

    private int totalQuitDays;
    private int totalQuitSmokes;
    private double totalSaveMoney;
}