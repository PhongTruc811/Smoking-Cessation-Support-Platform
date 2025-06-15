package com.group_7.backend.service.impl;

import com.group_7.backend.dto.UserMembershipDto;
import com.group_7.backend.entity.MembershipPackage;
import com.group_7.backend.entity.User;
import com.group_7.backend.entity.UserMembership;
import com.group_7.backend.entity.enums.MembershipStatusEnum;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.UserMembershipMapper;
import com.group_7.backend.repository.MembershipPackageRepository;
import com.group_7.backend.repository.UserMembershipRepository;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.IUserMembershipService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMembershipServiceImp implements IUserMembershipService {

    private final UserMembershipRepository userMembershipRepository;
    private final UserRepository userRepository;
    private final MembershipPackageRepository membershipPackageRepository;
    private final UserMembershipMapper userMembershipMapper;

    public UserMembershipServiceImp(
            UserMembershipRepository userMembershipRepository,
            UserRepository userRepository,
            MembershipPackageRepository membershipPackageRepository,
            UserMembershipMapper userMembershipMapper
    ) {
        this.userMembershipRepository = userMembershipRepository;
        this.userRepository = userRepository;
        this.membershipPackageRepository = membershipPackageRepository;
        this.userMembershipMapper = userMembershipMapper;
    }

    @Override
    @Transactional
    public UserMembershipDto create(UserMembershipDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        MembershipPackage membershipPackage = membershipPackageRepository.findById(dto.getMembershipPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("MembershipPackage not found with id: " + dto.getMembershipPackageId()));

        UserMembership userMembership = new UserMembership();
        userMembership.setUser(user);
        userMembership.setMembershipPackage(membershipPackage);
        userMembership.setStartDate(LocalDateTime.now());
        userMembership.setPaymentMethod(dto.getPaymentMethod());
        userMembership.setStatus(MembershipStatusEnum.ACTIVE);

        // Chỉnh ngày hết hạn
        if (membershipPackage.getDurationInDays() > 0) {
            LocalDate endDate = userMembership.getStartDate().toLocalDate().plusDays(membershipPackage.getDurationInDays());
            userMembership.setEndDate(endDate);
        }

        UserMembership saved = userMembershipRepository.save(userMembership);
        return userMembershipMapper.toDto(saved);
    }

    @Override
    public List<UserMembershipDto> getByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return userMembershipRepository.findByUserId(user.getUserId())
                .stream()
                .map(userMembershipMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserMembershipDto getCurrentMembership(Long userId) {
        UserMembership current = userMembershipRepository.findTopByUserIdAndStatusOrderByStartDateDesc(userId, MembershipStatusEnum.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("There is no active membership found for this user"));
        return userMembershipMapper.toDto(current);
    }

    @Override
    public UserMembershipDto getById(Long id) {
        UserMembership entity = userMembershipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserMembership not found with id: " + id));
        return userMembershipMapper.toDto(entity);
    }

    @Override
    public List<UserMembershipDto> getAll() {
        return userMembershipRepository.findAll()
                .stream()
                .map(userMembershipMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserMembershipDto update(Long id, UserMembershipDto dto) {
        UserMembership entity = userMembershipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserMembership not found with id: " + id));
        entity.setStatus(dto.getStatus());
        UserMembership saved = userMembershipRepository.save(entity);
        return userMembershipMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        UserMembership entity = userMembershipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserMembership not found with id: " + id));
        userMembershipRepository.delete(entity);
    }
}