package com.group_7.backend.dto.request;

import com.group_7.backend.entity.enums.UserGenderEnum;
import com.group_7.backend.entity.enums.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @Size(min = 5, max = 50, message = "Full name must be between 5 and 50 characters")
    private String fullName;

    @Email(message = "Email format is invalid")
    private String email;

    //TODO: thêm ràng buộc nhỏ hơn hiện tại
    private LocalDate dob;

    private UserGenderEnum gender;
}