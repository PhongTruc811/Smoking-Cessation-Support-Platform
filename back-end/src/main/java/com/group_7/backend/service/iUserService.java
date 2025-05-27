package com.group_7.backend.service;

import com.group_7.antismoking.dto.UserDto;

import java.util.List;

public interface iUserService {
    UserDto createUser(UserDto userDTO, String plainPassword);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    UserDto updateUser(Integer userId, UserDto userDTO);
    void deleteUser(Integer userId);
}
