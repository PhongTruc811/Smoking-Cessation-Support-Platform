package com.group_7.backend.service;

import com.group_7.backend.dto.RegRequestDto;
import com.group_7.backend.dto.UserDto;

import java.util.List;

public interface IUserService {
    UserDto createUser(UserDto userDTO, String plainPassword);
    UserDto getUserById(long userId);
    List<UserDto> getAllUsers();
    UserDto updateUser(long userId, UserDto userDTO);
    void deleteUser(long userId);
    UserDto authenticate(String usernameOrEmail, String password);
    UserDto register(RegRequestDto request);
}
