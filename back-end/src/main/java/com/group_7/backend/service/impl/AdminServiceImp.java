package com.group_7.backend.service.impl;

import com.group_7.backend.dto.UserDto;
import com.group_7.backend.entity.User;
import com.group_7.backend.entity.UserMembership;
import com.group_7.backend.entity.enums.MembershipStatusEnum;
import com.group_7.backend.entity.enums.UserRoleEnum;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.UserMapper;
import com.group_7.backend.mapper.UserMembershipMapper;
import com.group_7.backend.repository.UserMembershipRepository;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.IAdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImp implements IAdminService {

    private final UserRepository userRepository;
    private final UserMembershipRepository userMembershipRepository;
    private final UserMapper userMapper;
    private final UserMembershipMapper userMembershipMapper;

    public AdminServiceImp(UserRepository userRepository, UserMembershipRepository userMembershipRepository, UserMapper userMapper, UserMembershipMapper userMembershipMapper) {
        this.userRepository = userRepository;
        this.userMembershipRepository = userMembershipRepository;
        this.userMapper = userMapper;
        this.userMembershipMapper = userMembershipMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsersWithMembership() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            UserMembership currentMembership = userMembershipRepository
                    .findTopByUserUserIdAndStatusOrderByStartDateDesc(user.getUserId(), MembershipStatusEnum.ACTIVE)
                    .orElse(null);
            return userMapper.toDtoWithMembership(user, userMembershipMapper.toDto(currentMembership));
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto changeUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setStatus(!user.isStatus());
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public UserDto changeUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        UserRoleEnum roleEnum = UserRoleEnum.valueOf(newRole.toUpperCase());
        user.setRole(roleEnum);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }
}