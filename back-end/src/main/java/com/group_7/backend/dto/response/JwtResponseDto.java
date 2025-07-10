package com.group_7.backend.dto.response;

import com.group_7.backend.dto.QuitPlanDto;
import com.group_7.backend.dto.UserMembershipDto;
import com.group_7.backend.dto.request.RegRequestDto;
import com.group_7.backend.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponseDto extends ResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";
    private UserDto user;
    private UserMembershipDto currentUserMembership;
    private QuitPlanDto currentQuitPlan;

    public JwtResponseDto(String accessToken, UserDto user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    public JwtResponseDto(String status, String message, String accessToken, UserDto user, UserMembershipDto currentUserMembership, QuitPlanDto currentQuitPlan) {
        super(status, message);
        this.accessToken = accessToken;
        this.user = user;
        this.currentUserMembership = currentUserMembership;
        this.currentQuitPlan = currentQuitPlan;
    }

}