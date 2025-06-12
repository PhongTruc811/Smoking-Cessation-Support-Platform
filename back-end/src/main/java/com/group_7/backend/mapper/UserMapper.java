package com.group_7.backend.mapper;


import com.group_7.backend.dto.UserDto;
import com.group_7.backend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
    User toUser(UserDto userDto);
}
