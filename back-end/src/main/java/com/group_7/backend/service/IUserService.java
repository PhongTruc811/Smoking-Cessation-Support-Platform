package com.group_7.backend.service;

import com.group_7.backend.dto.RegRequestDto;
import com.group_7.backend.dto.UserDto;

import java.util.List;

public interface IUserService extends IBaseService<UserDto, Long>{
    UserDto create(UserDto userDTO, String plainPassword);
    UserDto authenticate(String usernameOrEmail, String password);
    UserDto register(RegRequestDto request);
}
