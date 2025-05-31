package com.group_7.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginReponseDto {
    String message;
    UserDto user;
    //Về sau thêm token khi dùng JWT
}
