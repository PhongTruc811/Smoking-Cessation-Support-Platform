package com.group_7.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.group_7.backend.entity.enums.UserGenderEnum;
import com.group_7.backend.entity.enums.UserRoleEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userId;
    private String username;
    private String fullName;
    private String email;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;

    private UserGenderEnum gender;
    private boolean status;
    private UserRoleEnum role;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdAt;

    private SmokingProfileDto smokingProfile;
    // Không trả về password vì bảo mật

    private UserMembershipDto currentUserMembership;
}