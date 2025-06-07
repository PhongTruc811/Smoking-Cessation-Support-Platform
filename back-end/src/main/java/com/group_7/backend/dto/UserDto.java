package com.group_7.backend.dto;

import com.group_7.backend.entity.enums.UserGenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
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
    private Set<String> roles;
    // Không trả về password vì bảo mật

}