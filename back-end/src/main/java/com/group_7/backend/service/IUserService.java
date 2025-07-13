package com.group_7.backend.service;

import com.group_7.backend.dto.request.PasswordRequestDto;
import com.group_7.backend.dto.request.RegRequestDto;
import com.group_7.backend.dto.UserDto;
import com.group_7.backend.dto.request.UserRequestDto;
import com.group_7.backend.entity.enums.UserRoleEnum;

import java.util.List;

public interface IUserService extends IBaseService<UserDto, UserRequestDto,Long>{
    UserDto changeStatus(Long userId);
    UserDto authenticate(String usernameOrEmail, String password);
    UserDto register(RegRequestDto request);
    void changePassword(Long userId, PasswordRequestDto request);

    List<UserDto> getAllByRole(UserRoleEnum role);
}
