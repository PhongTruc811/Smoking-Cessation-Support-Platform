package com.group_7.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "Username or email must not be empty")
    private String usernameOrEmail;

    @NotBlank(message = "Password must not be empty")
    private String password;
    //Về sau thêm JWT token
}
