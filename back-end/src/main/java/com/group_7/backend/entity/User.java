package com.group_7.backend.entity;

import com.group_7.backend.entity.enums.UserGenderEnum;
import com.group_7.backend.entity.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
@Getter
@Setter
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

    @Column(name = "Password", length = 100)
    private String password;

    @Column(name = "Dob")
    private LocalDate dob;

    @Column(name = "Status")
    private Boolean status = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "Gender", length = 10)
    private UserGenderEnum gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role", length = 30)
    private UserRoleEnum role;

    @Column(name = "CreatedAt")
    private LocalDate createdAt = LocalDate.now();

    //------------------------------------------------------------------------------
    //Thông tin hút thuốc của USER
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SmokingProfile smokingProfile;

    //Các memberhip USER mua
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserMembership> userMemberships = new HashSet<>();

    //Các kế hoạch cai thuốc của USER
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QuitPlan> quitPlans = new HashSet<>();

    //Like của USER trong posts/comments
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Like> likes = new HashSet<>();

    public void addQuitPlan(QuitPlan quitPlan) {
        this.quitPlans.add(quitPlan);
    }

}