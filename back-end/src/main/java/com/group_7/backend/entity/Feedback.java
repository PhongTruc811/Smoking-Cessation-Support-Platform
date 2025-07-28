package com.group_7.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FeedbackId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @Column(name = "ReceiverId", nullable = false) //Id là 0 cho hệ thống
    private Long receiverId;

    @Column(name = "Subject", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String subject;

    @Column(name = "Body", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String body;

    @Column(name = "Rating", nullable = false)
    private float rating;

    @Column(name = "CreatedAt", nullable = false)
    private LocalDateTime createdAt= LocalDateTime.now();
}