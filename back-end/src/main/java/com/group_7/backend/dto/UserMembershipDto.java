package com.group_7.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserMembershipDto {
    private long id;
    private LocalDateTime startDate;
    private LocalDate endDate;
    private String status;
    private String paymentMethod;
    private long userId;
    private long membershipPackageId;
}