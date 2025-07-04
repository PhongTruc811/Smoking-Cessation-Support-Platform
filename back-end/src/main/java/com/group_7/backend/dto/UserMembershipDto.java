package com.group_7.backend.dto;

import com.group_7.backend.entity.enums.MembershipStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMembershipDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private MembershipStatusEnum status;

    @NotNull(message = "Payment method must not be null")
    private String paymentMethod;

    @NotNull(message = "User ID must not be null")
    private Long userId;

    @NotNull(message = "Membership package ID must not be null")
    private Long membershipPackageId;

    @Override
    public String toString() {
        return "UserMembershipDto{" +
                "userId=" + userId +
                ", membershipPackageId=" + membershipPackageId +
                ", paymentMethod='" + paymentMethod + '\'' +
                // Thêm các trường khác nếu có
                '}';
    }
}