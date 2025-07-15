package com.group_7.backend.mapper;

import com.group_7.backend.dto.UserDto;
import com.group_7.backend.dto.UserMembershipDto;
import com.group_7.backend.entity.User;
import com.group_7.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    private final SmokingProfileMapper smokingProfileMapper;

    public UserMapper(SmokingProfileMapper smokingProfileMapper) {
        this.smokingProfileMapper = smokingProfileMapper;
    }

    // Entity -> DTO
    public UserDto toDto(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setDob(user.getDob());
        dto.setGender(user.getGender());
        dto.setStatus(user.isStatus());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setSmokingProfile(smokingProfileMapper.toDto(user.getSmokingProfile()));
        return dto;
    }

    // DTO -> Entity
    public User toEntity(UserDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setUsername(dto.getUsername());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setDob(dto.getDob());
        user.setGender(dto.getGender());
        user.setStatus(dto.isStatus());
        user.setRole(dto.getRole());
        return user;
    }

    public UserDto toDtoWithMembership(User user, UserMembershipDto membershipDto) {
        if (user == null) return null;

        // Bắt đầu tạo DTO từ đầu
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUsername(user.getUsername());
        userDto.setFullName(user.getFullName());
        userDto.setEmail(user.getEmail());
        userDto.setDob(user.getDob());
        userDto.setGender(user.getGender());
        userDto.setStatus(user.isStatus());
        userDto.setRole(user.getRole());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setSmokingProfile(smokingProfileMapper.toDto(user.getSmokingProfile()));

        // Gán thông tin membership
        userDto.setCurrentUserMembership(membershipDto);

        return userDto;
    }
}