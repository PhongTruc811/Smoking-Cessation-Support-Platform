package com.group_7.backend.dto.request;

import com.group_7.backend.entity.enums.UserGenderEnum;
import com.group_7.backend.entity.enums.UserRoleEnum;
import com.group_7.backend.util.custom.ValidLetter;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @Size(min = 5, max = 50, message = "Full name must be between 5 and 50 characters")
    @ValidLetter
    private String fullName;

    @Email(message = "Email format is invalid")
    private String email;

    @NotNull(message = "Date of birth must not be empty")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    private UserGenderEnum gender;
}