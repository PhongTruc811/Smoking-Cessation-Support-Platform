package com.group_7.backend.mapper;


import com.group_7.backend.dto.UserDto;
import com.group_7.backend.entity.User;

public class UserMapper {
    public static UserDto toDTO(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setDob(user.getDob());
        dto.setGender(user.getGender());
        dto.setStatus(user.getStatus());
        dto.setRole(user.getRole());
        return dto;
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setDob(dto.getDob());
        user.setGender(dto.getGender());
        user.setStatus(dto.getStatus());
        user.setRole(dto.getRole());
        return user;
    }
}
