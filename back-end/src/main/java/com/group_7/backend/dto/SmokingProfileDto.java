package com.group_7.backend.dto;

import com.group_7.backend.entity.enums.NicotineAddictionEnum;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmokingProfileDto {
    private Long smokingProfileId;
    private Long userId;

    @NotBlank(message = "Cigarettes per day must not be blank")
    @Size(max=100, message = "Input must not exceed 100 characters")
    private String cigarettesPerDay;

    @NotNull(message = "Cost per pack must not be null")
    @Positive(message = "Cost per pack must be greater than 0")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal costPerPack;

    @NotNull(message = "Weeks smoked must not be null")
    @Positive(message = "Weeks smoked must be greater than 0")
    private int weekSmoked;

    private NicotineAddictionEnum nicotineAddiction;

    private int ftndScore;

    @Size(max = 500, message = "Note must not exceed 500 characters")
    private String note;

    private LocalDate createAt;
    private LocalDate lastUpdateDate;
}