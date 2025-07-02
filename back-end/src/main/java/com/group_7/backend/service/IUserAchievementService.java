package com.group_7.backend.service;

import com.group_7.backend.dto.UserAchievementDto;

import java.util.List;

public interface IUserAchievementService{
    UserAchievementDto create(UserAchievementDto dto);
    UserAchievementDto getById(Long id);
    List<UserAchievementDto> getAll();
    List<UserAchievementDto> getByUserId(Long userId);
    void delete(Long id);
}