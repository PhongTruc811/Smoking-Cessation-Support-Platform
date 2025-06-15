package com.group_7.backend.dto;

import com.group_7.backend.entity.enums.MembershipStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMembershipDto {
    private long id;
    private LocalDateTime startDate;
    private LocalDate endDate;

    @NotNull(message = "Status must not be empty")
    private MembershipStatusEnum status;

    @NotNull(message = "Payment method must not be null")
    private String paymentMethod;

    @NotNull(message = "User ID must not be null")
    private long userId;

    @NotNull(message = "Membership package ID must not be null")
    private long membershipPackageId;
}