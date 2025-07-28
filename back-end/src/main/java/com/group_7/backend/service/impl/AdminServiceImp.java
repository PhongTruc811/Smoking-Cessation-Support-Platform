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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminServiceImp implements IAdminService {
    private static final Logger log = LoggerFactory.getLogger(AdminServiceImp.class);

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

        // --- BƯỚC 1: Lấy danh sách tất cả người dùng ---
        log.info("Admin request: Getting all users with membership...");
        System.out.println("Admin request: Getting all users with membership...");
        List<User> allUsers = userRepository.findAll();
        log.info("Found {} total users.", allUsers.size());

        if (allUsers.isEmpty()) {
            return List.of();
        }

        List<Long> userIds = allUsers.stream().map(User::getUserId).collect(Collectors.toList());

        List<UserMembership> activeMemberships = userMembershipRepository.findAllActiveMembershipsForUsers(userIds);
        log.info("Found {} active memberships for these users.", activeMemberships.size());

        Map<Long, UserMembership> membershipMap = activeMemberships.stream()
                .collect(Collectors.toMap(mem -> {
                    // Thêm log để kiểm tra xem getUser() có bị null không
                    if (mem.getUser() == null) {
                        log.error("CRITICAL: User is null for UserMembership ID: {}", mem.getId());
                        // Có thể ném lỗi ở đây để dừng lại, hoặc return một giá trị không thể có
                        return -1L;
                    }
                    return mem.getUser().getUserId();
                }, mem -> mem));

        List<UserDto> result = allUsers.stream().map(user -> {
            UserMembership currentMembership = membershipMap.get(user.getUserId());
            // Log ra để xem user nào có/không có membership
            if (currentMembership != null) {
                log.info("Mapping: User ID {} has ACTIVE membership.", user.getUserId());
            } else {
                log.info("Mapping: User ID {} has NO active membership.", user.getUserId());
            }
            return userMapper.toDtoWithMembership(user, userMembershipMapper.toDto(currentMembership));
        }).collect(Collectors.toList());

        log.info("Finished mapping users to DTOs.");
        return result;
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