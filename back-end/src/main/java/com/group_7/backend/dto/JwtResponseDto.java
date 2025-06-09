package com.group_7.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseDto extends RegRequestDto{
    private String accessToken;
    private String tokenType = "Bearer";
    private UserDto user;

    public JwtResponseDto(String accessToken, UserDto user) {
        this.accessToken = accessToken;
        this.user = user;
    }
}