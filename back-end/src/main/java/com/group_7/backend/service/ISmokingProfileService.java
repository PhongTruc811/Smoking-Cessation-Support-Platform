package com.group_7.backend.service;

import com.group_7.backend.dto.SmokingProfileDto;
import com.group_7.backend.dto.request.UserQuizAnswerRequestDto;

public interface ISmokingProfileService extends ICRUDService<SmokingProfileDto, SmokingProfileDto, Long> {
    SmokingProfileDto getByUserId(Long userId);
    SmokingProfileDto submitUserQuiz(UserQuizAnswerRequestDto requestDto);
}