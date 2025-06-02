package com.group_7.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class RegRequestDto {
    @NotBlank(message = "Username không được để trống")
    @Size(min = 5, max = 30, message = "Username phải từ 5-30 ký tự")
    private String username;

    @Size(min = 5, max = 50, message = "Họ tên phải có từ 5-50 ký tự ")
    private String fullName;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    private LocalDate dob;

    @Pattern(regexp = "^[MFO]$", message = "Giới tính chỉ nhận M, F hoặc O")
    private String gender;
}
