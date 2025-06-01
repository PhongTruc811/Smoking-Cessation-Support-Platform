package com.group_7.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "Tên đăng nhập hoặc email không được để trống")
    private String usernameOrEmail;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
    //Về sau thêm JWT token
}
