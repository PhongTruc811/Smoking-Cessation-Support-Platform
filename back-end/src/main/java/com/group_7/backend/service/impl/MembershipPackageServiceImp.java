package com.group_7.backend.service.impl;

import com.group_7.backend.dto.MembershipPackageDto;
import com.group_7.backend.entity.MembershipPackage;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.MembershipPackageMapper;
import com.group_7.backend.repository.MembershipPackageRepository;
import com.group_7.backend.service.IMembershipPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembershipPackageServiceImp implements IMembershipPackageService {

    private final MembershipPackageRepository membershipPackageRepository;
    private final MembershipPackageMapper membershipPackageMapper;


    public MembershipPackageServiceImp(
            MembershipPackageRepository membershipPackageRepository,
            MembershipPackageMapper membershipPackageMapper) {
        this.membershipPackageRepository = membershipPackageRepository;
        this.membershipPackageMapper = membershipPackageMapper;
    }

    @Override
    public MembershipPackageDto getById(Long id) {
        MembershipPackage entity = membershipPackageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MembershipPackage not found with id: " + id));
        return membershipPackageMapper.toDto(entity);
    }

    @Override
    public List<MembershipPackageDto> getAll() {
        return membershipPackageRepository.findAll()
                .stream()
                .map(membershipPackageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public MembershipPackageDto update(Long id, MembershipPackageDto dto) {
        MembershipPackage packageEntity = membershipPackageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MembershipPackage not found with id: " + id));

        packageEntity.setPrice(dto.getPrice());
        packageEntity.setDescription(dto.getDescription());
        packageEntity.setDurationInDays(dto.getDurationInDays());
        packageEntity.setPackageName(dto.getPackageName());
        packageEntity.setActive(dto.isActive());
        MembershipPackage saved = membershipPackageRepository.save(packageEntity);
        return membershipPackageMapper.toDto(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(Long id) {
        MembershipPackage entity = membershipPackageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MembershipPackage not found with id: " + id));
        membershipPackageRepository.delete(entity);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public MembershipPackageDto create(MembershipPackageDto dto) {
        MembershipPackage entity = membershipPackageMapper.toEntity(dto);
        return membershipPackageMapper.toDto(membershipPackageRepository.save(entity));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public MembershipPackageDto changeActive(Long id) {
        MembershipPackage entity = membershipPackageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MembershipPackage not found with id: " + id));
        entity.setActive(!entity.isActive());
        membershipPackageRepository.save(entity);
        return membershipPackageMapper.toDto(entity);
    }
}