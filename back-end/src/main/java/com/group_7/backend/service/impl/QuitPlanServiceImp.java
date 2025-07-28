package com.group_7.backend.service.impl;

import com.group_7.backend.dto.QuitMethodOptionDto;
import com.group_7.backend.dto.QuitPlanDto;
import com.group_7.backend.entity.*;
import com.group_7.backend.entity.enums.QuitPlanStatusEnum;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.QuitMethodOptionMapper;
import com.group_7.backend.mapper.QuitPlanMapper;
import com.group_7.backend.repository.*;
import com.group_7.backend.service.IQuitPlanService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuitPlanServiceImp implements IQuitPlanService {

    private final QuitPlanRepository quitPlanRepository;
    private final QuitMethodOptionRepository quitMethodOptionRepository;
    private final UserRepository userRepository;
    private final UserRecordRepository userRecordRepository;
    private final QuitPlanMapper quitPlanMapper;
    private final SmokingProfileRepository smokingProfileRepository;

    public QuitPlanServiceImp(QuitPlanRepository quitPlanRepository, QuitMethodOptionRepository quitMethodOptionRepository,
                              UserRepository userRepository, UserRecordRepository userRecordRepository,
                              QuitPlanMapper quitPlanMapper, SmokingProfileRepository smokingProfileRepository) {
        this.quitPlanRepository = quitPlanRepository;
        this.quitMethodOptionRepository = quitMethodOptionRepository;
        this.userRepository = userRepository;
        this.userRecordRepository = userRecordRepository;
        this.quitPlanMapper = quitPlanMapper;
        this.smokingProfileRepository = smokingProfileRepository;
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

    @Override
    @Transactional
    @PreAuthorize("#dto.userId == authentication.principal.id or hasAuthority('ROLE_COACH')")
    public QuitPlanDto update(Long id, QuitPlanDto dto) {
        QuitPlan plan = quitPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuitPlan not found with id: " + id));
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

    public QuitPlanDto updateTotalSmoke(Long id, int newSmoke) {
        QuitPlan plan = quitPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuitPlan not found with id: " + id));
        UserRecord userRecord = userRecordRepository.findByUserUserId(plan.getUser().getUserId());
        SmokingProfile smokingProfile= smokingProfileRepository.findByUserUserId(plan.getUser().getUserId());

        System.out.println(plan.getTotalSmoke()+"+"+newSmoke);
        plan.setTotalSmoke(plan.getTotalSmoke()+newSmoke);
        userRecord.setTotalQuitSmokes(userRecord.getTotalQuitSmokes()+newSmoke);
        BigDecimal newSaveMoney = BigDecimal.valueOf(newSmoke)//Tiền tiết kiệm sau khi update
                .multiply(smokingProfile.getCostPerPack())
                .divide(BigDecimal.valueOf(20), 2, RoundingMode.HALF_UP);
        userRecord.setTotalSaveMoney(userRecord.getTotalSaveMoney().add(newSaveMoney));

        userRecordRepository.save(userRecord);
        return quitPlanMapper.toDto(quitPlanRepository.save(plan));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) auth.getPrincipal();
        QuitPlan entity = quitPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuitPlan not found with id: " + id));
        if (customUserDetail.getId()!=entity.getUser().getUserId()){
            throw new ResourceAccessException("You do not have permission to delete this quit plan");
        }
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
    public QuitPlanDto getCurrentByUserIdAndStatus(Long userId) {
        Optional<QuitPlan> inProgressPlan = quitPlanRepository.findTopByUserUserIdAndStatus(userId, QuitPlanStatusEnum.IN_PROGRESS);
        if (inProgressPlan.isPresent()) {
            return quitPlanMapper.toDto(inProgressPlan.get());
        }

        Optional<QuitPlan> scheduledPlan = quitPlanRepository.findTopByUserUserIdAndStatus(userId, QuitPlanStatusEnum.SCHEDULED);
        if (scheduledPlan.isPresent()) {
            return quitPlanMapper.toDto(scheduledPlan.get());
        }

        return null;
    }

    @Override
    public List<QuitPlanDto> getByUserIdAndStatus(Long userId, QuitPlanStatusEnum status) {
        return quitPlanRepository.findByUserUserIdAndStatus(userId, status)
                .stream()
                .map(quitPlanMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("#dto.userId == authentication.principal.id or hasAuthority('ROLE_ADMIN')")
    public QuitPlanDto create(QuitPlanDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        if (quitPlanRepository.existsByUserUserIdAndStatus(dto.getUserId(), QuitPlanStatusEnum.IN_PROGRESS)||
                quitPlanRepository.existsByUserUserIdAndStatus(dto.getUserId(), QuitPlanStatusEnum.SCHEDULED)) {
            throw new IllegalArgumentException("This user already have active quit plan!");
        }
        QuitPlan entity = quitPlanMapper.toEntity(dto, user);
        if (entity.getStartDate().isEqual(LocalDate.now())) {
            entity.setStatus(QuitPlanStatusEnum.IN_PROGRESS);
        }
        else entity.setStatus(QuitPlanStatusEnum.SCHEDULED);
        //Gán startDate cho từng Stage và check valid date

        Set<QuitMethodOption> saveMethodOptions = new HashSet<>();
        if (dto.getMethodOptions() != null && !dto.getMethodOptions().isEmpty()) {
            for (QuitMethodOptionDto optionDto : dto.getMethodOptions()) {
                //Kiểm tra option được gửi qua request có hợp lệ
                if (optionDto.getId() != null) {
                    QuitMethodOption existingOption = quitMethodOptionRepository.findById(optionDto.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("QuitMethodOption not found ! Cannot link to a non-existent option."));
                    saveMethodOptions.add(existingOption);
                } else {
                    throw new IllegalArgumentException("Cannot create new QuitMethodOption via QuitPlan creation. Please provide an existing option ID.");
                }
            }
        }
        entity.setMethodOptions(saveMethodOptions);

        return quitPlanMapper.toDto(quitPlanRepository.save(entity));
    }
}