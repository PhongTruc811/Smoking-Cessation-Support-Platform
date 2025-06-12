package com.group_7.backend.dto;

import java.math.BigDecimal;

public class MembershipPackageDto {
    private long id;
    private String packageName;
    private String description;
    private BigDecimal price;
    private int durationInDays;
    private boolean isActive;
}