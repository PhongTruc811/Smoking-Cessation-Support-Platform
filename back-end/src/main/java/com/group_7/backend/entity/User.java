package com.group_7.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "UserID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "Username",nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "FullName",length = 50)
    private String fullName;

    @Column(name = "Email",nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "Password",nullable = false, length = 100)
    private String password;

    @Column(name = "DOB")
    private LocalDate dob;

    @Column(name = "Gender", length = 1)
    private String gender; // 'M' or 'F' or 'O' (others)

    @Column(name = "Status")
    private Boolean status = true;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt = LocalDateTime.now();

}