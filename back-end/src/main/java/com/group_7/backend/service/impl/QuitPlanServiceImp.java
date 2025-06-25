package com.group_7.backend.service.impl;

import com.group_7.backend.dto.QuitPlanDto;
import com.group_7.backend.entity.QuitPlan;
import com.group_7.backend.entity.QuitPlanStage;
import com.group_7.backend.entity.User;
import com.group_7.backend.entity.enums.QuitPlanStatusEnum;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.QuitPlanMapper;
import com.group_7.backend.repository.QuitPlanRepository;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.IQuitPlanService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
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
    @PreAuthorize("#userId == authentication.principal.id or hasAuthority('ROLE_ADMIN')")
    public QuitPlanDto getById(Long id) {
        QuitPlan entity = quitPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuitPlan not found with id: " + id));
        return quitPlanMapper.toDto(entity);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<QuitPlanDto> getAll() {
        return quitPlanRepository.findAll()
                .stream()
                .map(quitPlanMapper::toDto)
                .collect(Collectors.toList());
    }

    //@Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.id or hasAuthority('ROLE_ADMIN')")
    public QuitPlanDto create2(QuitPlanDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        QuitPlan entity = quitPlanMapper.toEntity(dto, user);
        QuitPlan saved = quitPlanRepository.save(entity);
        return quitPlanMapper.toDto(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.id or hasAuthority('ROLE_COACH')")
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
    @PreAuthorize("#userId == authentication.principal.id or hasAuthority('ROLE_COACH')")
    public QuitPlanDto updateStatus(Long id, QuitPlanStatusEnum status) {
        QuitPlan plan = quitPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuitPlan not found with id: " + id));
        plan.setStatus(status);
        QuitPlan saved = quitPlanRepository.save(plan);
        return quitPlanMapper.toDto(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.id")
    public void delete(Long id) {
        QuitPlan entity = quitPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuitPlan not found with id: " + id));
        entity.setStatus(QuitPlanStatusEnum.CANCELLED);
        quitPlanRepository.save(entity);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_COACH')")
    public List<QuitPlanDto> getByUserId(Long userId) {
        return quitPlanRepository.findByUserUserId(userId)
                .stream()
                .map(quitPlanMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.id or hasAuthority('ROLE_ADMIN')")
    public QuitPlanDto create(QuitPlanDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        QuitPlan entity = quitPlanMapper.toEntity(dto, user);
        if (entity.getStartDate().isEqual(LocalDate.now())) {
            entity.setStatus(QuitPlanStatusEnum.IN_PROGRESS);
        }
        else entity.setStatus(QuitPlanStatusEnum.SCHEDULED);
        //Gán startDate cho từng Stage và check valid date
        AssignStageDates(entity);

        QuitPlan saved = quitPlanRepository.save(entity);
        return quitPlanMapper.toDto(saved);
    }

    public static void AssignStageDates(QuitPlan plan) {
        List<QuitPlanStage> stages = plan.getQuitPlanStages().stream()
                .sorted(Comparator.comparingInt(QuitPlanStage::getStageNumber))
                .toList();

        LocalDate planStart = plan.getStartDate();
        LocalDate planEnd = plan.getTargetEndDate();
        int totalDays = (int) (planEnd.toEpochDay() - planStart.toEpochDay());

        int sumDuration = stages.stream().mapToInt(QuitPlanStage::getDuration).sum();
        if (sumDuration != totalDays) {
            throw new IllegalArgumentException("Invalid date of quit smoking plan!");
        }

        // Gán startDate cho từng stage
        for (QuitPlanStage stage : stages) {
            stage.setStartDate(planStart);
            planStart = planStart.plusDays(stage.getDuration());
        }
    }
}