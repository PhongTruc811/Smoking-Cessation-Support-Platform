package com.group_7.backend.service.impl;

import com.group_7.backend.dto.FeedbackDto;
import com.group_7.backend.entity.Feedback;
import com.group_7.backend.entity.User;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.FeedbackMapper;
import com.group_7.backend.repository.FeedbackRepository;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.IFeedbackService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImp implements IFeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final FeedbackMapper feedbackMapper;

    public FeedbackServiceImp(FeedbackRepository feedbackRepository, UserRepository userRepository, FeedbackMapper feedbackMapper) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.feedbackMapper = feedbackMapper;
    }

    @Override
    @Transactional
    public FeedbackDto create(FeedbackDto feedbackDto) {
        User user = userRepository.findById(feedbackDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if (feedbackDto.getReceiverId() != 0) {
            userRepository.findById(feedbackDto.getReceiverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Receiver user not found!" ));
        }

        if (feedbackRepository.existsByReceiverIdAndUser_UserId(feedbackDto.getReceiverId(), feedbackDto.getUserId())) {
            return update(feedbackRepository.findByReceiverIdAndUser_UserId(feedbackDto.getReceiverId(), feedbackDto.getUserId()).getId(), feedbackDto);
        }

        Feedback feedback = feedbackMapper.toEntity(feedbackDto, user);

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return feedbackMapper.toDto(savedFeedback);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackDto getById(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + id));
        return feedbackMapper.toDto(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackDto> getAll() {
        return feedbackRepository.findAll()
                .stream().map(feedbackMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackDto> getByUserId(Long userId) {
        return feedbackRepository.findByUser_UserId(userId)
                .stream().map(feedbackMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackDto> getByReceiverId(Long receiverId) {
        return feedbackRepository.findByReceiverId(receiverId)
                .stream().map(feedbackMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FeedbackDto update(Long id, FeedbackDto feedbackDto) {
        Feedback existingFeedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + id));


        existingFeedback.setRating(feedbackDto.getRating());
        existingFeedback.setBody(feedbackDto.getBody());
        existingFeedback.setCreatedAt(LocalDateTime.now());
        Feedback updatedFeedback = feedbackRepository.save(existingFeedback);
        return feedbackMapper.toDto(updatedFeedback);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new ResourceNotFoundException("Feedback not found with id: " + id);
        }
        feedbackRepository.deleteById(id);
    }
}