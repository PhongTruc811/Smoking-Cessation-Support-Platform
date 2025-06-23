package com.group_7.backend.service.impl;

import com.group_7.backend.dto.SmokingProfileDto;
import com.group_7.backend.entity.SmokingProfile;
import com.group_7.backend.entity.User;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.SmokingProfileMapper;
import com.group_7.backend.repository.SmokingProfileRepository;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.ISmokingProfileService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SmokingProfileServiceImp implements ISmokingProfileService {

    private final SmokingProfileRepository smokingProfileRepository;
    private final UserRepository userRepository;
    private final SmokingProfileMapper smokingProfileMapper;

    public SmokingProfileServiceImp(
            SmokingProfileRepository smokingProfileRepository,
            UserRepository userRepository,
            SmokingProfileMapper smokingProfileMapper) {
        this.smokingProfileRepository = smokingProfileRepository;
        this.userRepository = userRepository;
        this.smokingProfileMapper = smokingProfileMapper;
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public SmokingProfileDto getById(Long id) {
        SmokingProfile entity = smokingProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SmokingProfile not found with id: " + id));
        return smokingProfileMapper.toDto(entity);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<SmokingProfileDto> getAll() {
        return smokingProfileRepository.findAll()
                .stream()
                .map(smokingProfileMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.id")
    public SmokingProfileDto update(Long id, SmokingProfileDto dto) {
        SmokingProfile profile = smokingProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SmokingProfile not found with id: " + id));

        profile.setCigarettesPerDay(dto.getCigarettesPerDay());
        profile.setCostPerPack(dto.getCostPerPack());
        profile.setWeekSmoked(dto.getWeekSmoked());
        profile.setNote(dto.getNote());

        SmokingProfile saved = smokingProfileRepository.save(profile);
        return smokingProfileMapper.toDto(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(Long id) {
        SmokingProfile entity = smokingProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SmokingProfile not found with id: " + id));
        smokingProfileRepository.delete(entity);
    }

    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.id")
    public SmokingProfileDto create(SmokingProfileDto dto) {
        if (smokingProfileRepository.existsByUserUserId(dto.getUserId())) {
            throw new IllegalArgumentException("Smoking profile for this user already exists");
        }
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        SmokingProfile entity = smokingProfileMapper.toEntity(dto, user);

        SmokingProfile saved = smokingProfileRepository.save(entity);
        return smokingProfileMapper.toDto(saved);
    }

    @Override
    public SmokingProfileDto getByUserId(Long userId) {
        SmokingProfile entity = smokingProfileRepository.findByUserUserId(userId);
        if (entity == null) {
            throw new ResourceNotFoundException("SmokingProfile not found for user id: " + userId);
        }
        return smokingProfileMapper.toDto(entity);
    }
}