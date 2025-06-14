package com.group_7.backend.service;

import com.group_7.backend.dto.SmokingProfileDto;

public interface ISmokingProfileService extends ICRUDService<SmokingProfileDto, SmokingProfileDto, Long> {
    SmokingProfileDto getByUserId(Long userId);
}