package com.group_7.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MembershipPackages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MembershipPackage {

    @Id
    @Column(name = "PackageID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PackageName", columnDefinition = "NVARCHAR(100)")
    private String packageName;

    @Column(name = "Description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "Price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "DurationInDays", nullable = false)
    private int durationInDays;

    @Column(name = "IsActive")
    private boolean isActive = true;

    //------------------------------------------------------------------------------
    @OneToMany(mappedBy = "membershipPackage",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, // Không dùng ALL vì không xóa lịch sử đăng ký khi xóa pack
            fetch = FetchType.LAZY)
    private Set<UserMembership> userMemberships = new HashSet<>();

}