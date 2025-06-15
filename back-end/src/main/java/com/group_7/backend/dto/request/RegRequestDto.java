package com.group_7.backend.dto.request;

import com.group_7.backend.entity.enums.UserGenderEnum;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegRequestDto {
    @NotBlank(message = "Username must not be empty")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;

    @NotBlank(message = "Full name must not be empty")
    @Size(min = 5, max = 50, message = "Full name must be between 5 and 50 characters")
    private String fullName;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Size(max = 100, message = "Password must not exceed 100 characters")
    private String password;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email format is invalid")
    private String email;

    @NotNull(message = "Date of birth must not be empty")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotNull(message = "Gender must not be empty")
    private UserGenderEnum gender;
}