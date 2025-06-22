package com.group_7.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequestDto {
    @NotBlank(message = "Password must not be empty")
    private String currentPassword;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 6, message = "New password must be at least 6 characters")
    private String newPassword;
}
