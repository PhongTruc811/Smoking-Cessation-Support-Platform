package com.group_7.backend.service.impl;

import com.group_7.backend.dto.QuitPlanDto;
import com.group_7.backend.entity.QuitPlan;
import com.group_7.backend.entity.User;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.QuitPlanMapper;
import com.group_7.backend.repository.QuitPlanRepository;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.IQuitPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuitPlanServiceImp implements IQuitPlanService {

    private final QuitPlanRepository quitPlanRepository;
    private final UserRepository userRepository;
    private final QuitPlanMapper quitPlanMapper;

    public QuitPlanServiceImp(QuitPlanRepository quitPlanRepository,
                              UserRepository userRepository,
                              QuitPlanMapper quitPlanMapper) {
        this.quitPlanRepository = quitPlanRepository;
        this.userRepository = userRepository;
        this.quitPlanMapper = quitPlanMapper;
    }

    @Override
    public QuitPlanDto getById(Long id) {
        QuitPlan entity = quitPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuitPlan not found with id: " + id));
        return quitPlanMapper.toDto(entity);
    }

    @Override
    public List<QuitPlanDto> getAll() {
        return quitPlanRepository.findAll()
                .stream()
                .map(quitPlanMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuitPlanDto create(QuitPlanDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        QuitPlan entity = quitPlanMapper.toEntity(dto, user);
        QuitPlan saved = quitPlanRepository.save(entity);
        return quitPlanMapper.toDto(saved);
    }

    @Override
    @Transactional
    public QuitPlanDto update(Long id, QuitPlanDto dto) {
        QuitPlan plan = quitPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuitPlan not found with id: " + id));
        plan.setReason(dto.getReason());
        plan.setStartDate(dto.getStartDate());
        plan.setTargetEndDate(dto.getTargetEndDate());
        plan.setStatus(dto.getStatus());
        // Không cập nhật userId và createdAt để giữ nguyên thông tin gốc
        QuitPlan saved = quitPlanRepository.save(plan);
        return quitPlanMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        QuitPlan entity = quitPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuitPlan not found with id: " + id));
        quitPlanRepository.delete(entity);
    }

    @Override
    public List<QuitPlanDto> getByUserId(Long userId) {
        return quitPlanRepository.findByUserUserId(userId)
                .stream()
                .map(quitPlanMapper::toDto)
                .collect(Collectors.toList());
    }
}