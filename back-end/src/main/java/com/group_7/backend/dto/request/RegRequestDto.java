package com.group_7.backend.dto.request;

import com.group_7.backend.entity.enums.UserGenderEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @Size(min = 5, max = 50, message = "Full name must be between 5 and 50 characters")
    private String fullName;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email format is invalid")
    private String email;

    //TODO: thêm ràng buộc nhỏ hơn hiện tại
    private LocalDate dob;

    private UserGenderEnum gender;
}