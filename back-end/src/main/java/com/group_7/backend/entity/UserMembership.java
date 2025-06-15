package com.group_7.backend.entity;

import com.group_7.backend.entity.enums.MembershipStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity trung gian, chứa thông tin về việc một User đăng ký một MembershipPackage.
 * Đây là phía sở hữu (owning side) cho cả hai mối quan hệ Many-to-One.
 */
@Entity
@Table(name = "UserMemberships")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMembership {

    @Id
    @Column(name = "UserMembershipID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "StartDate")
    private LocalDateTime startDate;//Không cần set datenow vì service sẽ xử lí cho start và end Date

    @Column(name = "EndDate")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", length = 50)
    private MembershipStatusEnum status;

    @Column(name = "PaymentMethod", length = 50)
    private String paymentMethod;

    //------------------------------------------------------------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PackageID", nullable = false)
    private MembershipPackage membershipPackage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMembership that = (UserMembership) o;
        return id != 0 && id == that.id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}