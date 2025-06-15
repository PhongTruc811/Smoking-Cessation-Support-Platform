package com.group_7.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "Username or email must not be empty")
    @Size(min = 3, max = 50, message = "Username or email must be between 3 and 50 characters")
    private String usernameOrEmail;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Size(max = 100, message = "Password must not exceed 100 characters")
    private String password;
}