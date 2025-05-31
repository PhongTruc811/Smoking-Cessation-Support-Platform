package com.group_7.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegRequestDto {
    private String username;
    private String fullName;
    private String password;
    private String email;
    private LocalDate dob;
    private String gender;
}