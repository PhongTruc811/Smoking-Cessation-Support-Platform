package com.group_7.backend.service;

import com.group_7.backend.dto.FeedbackDto;

import java.util.List;

public interface IFeedbackService extends ICRUDService<FeedbackDto, FeedbackDto, Long>{
    List<FeedbackDto> getByUserId(Long userId);
    List<FeedbackDto> getByReceiverId(Long receiverId);
}
