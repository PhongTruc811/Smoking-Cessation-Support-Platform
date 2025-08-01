package com.group_7.backend.service.impl;

import com.group_7.backend.dto.MembershipPackageDto;
import com.group_7.backend.entity.MembershipPackage;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.MembershipPackageMapper;
import com.group_7.backend.repository.MembershipPackageRepository;
import com.group_7.backend.service.IMembershipPackageService;
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

        // Chỉ kiểm tra khi Admin đang cố gắng DEACTIVATE (vô hiệu hóa) một gói
        boolean isDeactivating = !dto.isActive() && packageEntity.isActive();

        if (isDeactivating) {
            long activePackagesCount = membershipPackageRepository.countByIsActive(true);
            if (activePackagesCount <= 1) {
                // Nếu chỉ còn 1 (hoặc 0) gói active, không cho phép vô hiệu hóa gói cuối cùng này
                throw new IllegalStateException("Cannot deactivate the last active package. At least one package must be active.");
            }
        }

        // Nếu kiểm tra qua, tiếp tục cập nhật thông tin
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
        // --- THÊM LOGIC KIỂM TRA TƯƠNG TỰ VÀO ĐÂY ---
        long activePackagesCount = membershipPackageRepository.countByIsActive(true);
        if (entity.isActive() && activePackagesCount <= 1) {
            throw new IllegalStateException("Cannot deactivate the last active package. At least one package must be active.");
        }
        entity.setActive(false);
        membershipPackageRepository.save(entity);
        //membershipPackageRepository.delete(entity);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public MembershipPackageDto create(MembershipPackageDto dto) {
        // Cho phép tạo gói mới thoải mái
        MembershipPackage entity = membershipPackageMapper.toEntity(dto);

        // Mặc định tạo gói mới là INACTIVE để Admin phải chủ động kích hoạt
        entity.setActive(false);

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