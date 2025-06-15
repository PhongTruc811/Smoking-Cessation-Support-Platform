package com.group_7.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmokingProfileDto {
    private long smokingProfileId;
    private long userId;

    @NotNull(message = "Cigarettes per day must not be null")
    @Positive(message = "Cigarettes per day must be greater than 0")
    private Integer cigarettesPerDay;

    @NotNull(message = "Cost per pack must not be null")
    @Positive(message = "Cost per pack must be greater than 0")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Double costPerPack;

    @NotNull(message = "Weeks smoked must not be null")
    @Positive(message = "Weeks smoked must be greater than 0")
    private Integer weekSmoked;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String note;
}