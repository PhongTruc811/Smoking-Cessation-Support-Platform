package com.group_7.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "UserAchievement")
public class UserAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    // User nhận achievement
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId")
    private User user;

    // Achievement mà user đạt được
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AchievementId")
    private Achievement achievement;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt = LocalDateTime.now();
}