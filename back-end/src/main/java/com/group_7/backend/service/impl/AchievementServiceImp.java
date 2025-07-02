package com.group_7.backend.service.impl;

import com.group_7.backend.dto.AchievementDto;
import com.group_7.backend.entity.Achievement;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.AchievementMapper;
import com.group_7.backend.repository.AchievementRepository;
import com.group_7.backend.service.IAchievementService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementServiceImp implements IAchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;

    public AchievementServiceImp(AchievementRepository achievementRepository, AchievementMapper achievementMapper) {
        this.achievementRepository = achievementRepository;
        this.achievementMapper = achievementMapper;
    }

    @Override
    public AchievementDto getById(Long id) {
        Achievement entity = achievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found with id: " + id));
        return achievementMapper.toDto(entity);
    }

    @Override
    public List<AchievementDto> getAll() {
        return achievementRepository.findAll()
                .stream().map(achievementMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public AchievementDto update(Long id, AchievementDto dto) {
        Achievement entity = achievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found with id: " + id));
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setImageUrl(dto.getImageUrl());
        return achievementMapper.toDto(achievementRepository.save(entity));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(Long id) {
        Achievement entity = achievementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found with id: " + id));
        achievementRepository.delete(entity);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public AchievementDto create(AchievementDto dto) {
        Achievement entity = achievementMapper.toEntity(dto);
        return achievementMapper.toDto(achievementRepository.save(entity));
    }
}