package com.group_7.backend.dto;

import com.group_7.backend.entity.enums.UserGenderEnum;
import com.group_7.backend.entity.enums.UserRoleEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long userId;
    private String username;
    private String fullName;
    private String email;
    private LocalDate dob;
    private UserGenderEnum gender;
    private Boolean status;
    private UserRoleEnum role;
    // Không trả về password vì bảo mật

}