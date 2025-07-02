package com.group_7.backend.service.impl;

import com.group_7.backend.dto.UserAchievementDto;
import com.group_7.backend.entity.Achievement;
import com.group_7.backend.entity.User;
import com.group_7.backend.entity.UserAchievement;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.UserAchievementMapper;
import com.group_7.backend.repository.AchievementRepository;
import com.group_7.backend.repository.UserAchievementRepository;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.IUserAchievementService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAchievementServiceImp implements IUserAchievementService {
    private final UserAchievementRepository userAchievementRepository;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementMapper userAchievementMapper;

    public UserAchievementServiceImp(
            UserAchievementRepository userAchievementRepository,
            UserRepository userRepository,
            AchievementRepository achievementRepository,
            UserAchievementMapper userAchievementMapper) {
        this.userAchievementRepository = userAchievementRepository;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
        this.userAchievementMapper = userAchievementMapper;
    }

    @Override
    public UserAchievementDto getById(Long id) {
        UserAchievement entity = userAchievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserAchievement not found with id: " + id));
        return userAchievementMapper.toDto(entity);
    }

    @Override
    public List<UserAchievementDto> getAll() {
        return userAchievementRepository.findAll()
                .stream().map(userAchievementMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        UserAchievement entity = userAchievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserAchievement not found with id: " + id));
        userAchievementRepository.delete(entity);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserAchievementDto create(UserAchievementDto dto) {
        if (userAchievementRepository.existsByUserUserIdAndAchievementAchievementId(dto.getUserId(), dto.getAchievementId())) {
            throw new IllegalArgumentException("User has already achieved this achievement");
        }
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        Achievement achievement = achievementRepository.findById(dto.getAchievementId())
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found with id: " + dto.getAchievementId()));
        UserAchievement entity = userAchievementMapper.toEntity(user, achievement);
        userAchievementRepository.save(entity);
        return userAchievementMapper.toDto(entity);
    }

    @Override
    public List<UserAchievementDto> getByUserId(Long userId) {
        return userAchievementRepository.findByUserUserId(userId)
                .stream().map(userAchievementMapper::toDto)
                .collect(Collectors.toList());
    }

}