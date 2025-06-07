package com.group_7.backend.entity;

import com.group_7.backend.entity.enums.UserGenderEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "UserID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(name = "Username",nullable = false, unique = true, length = 30)
    private String username;

    @Column(name = "FullName", columnDefinition = "NVARCHAR(50)")
    private String fullName;

    @Column(name = "Email",nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "Password",nullable = false, length = 100)
    private String password;

    @Column(name = "DOB")
    private LocalDate dob;

    @Column(name = "Status")
    private Boolean status = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "Gender", length = 10)
    private UserGenderEnum gender;

    @Column(name = "Roles", length = 30)
    private Set<String> roles;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private SmokingProfile smokingProfile;
}