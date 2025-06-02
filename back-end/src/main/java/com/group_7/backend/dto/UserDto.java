package com.group_7.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer userId;
    private String username;
    private String fullName;
    private String email;
    private LocalDate dob;
    private String gender;
    private Boolean status;
    private String role;
    // Không trả về password vì bảo mật

}