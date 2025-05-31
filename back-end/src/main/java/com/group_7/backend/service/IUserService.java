package com.group_7.backend.service;

import com.group_7.backend.dto.RegRequestDto;
import com.group_7.backend.dto.UserDto;

import java.util.List;

public interface IUserService {
    UserDto createUser(UserDto userDTO, String plainPassword);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    UserDto updateUser(Integer userId, UserDto userDTO);
    void deleteUser(Integer userId);
    UserDto authenticate(String usernameOrEmail, String password);
    UserDto register(RegRequestDto request);
}
