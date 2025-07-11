package com.group_7.backend.service.impl;

import com.group_7.backend.dto.UserMembershipDto;
import com.group_7.backend.entity.MembershipPackage;
import com.group_7.backend.entity.User;
import com.group_7.backend.entity.UserMembership;
import com.group_7.backend.entity.enums.MembershipStatusEnum;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.UserMembershipMapper;
import com.group_7.backend.repository.MembershipPackageRepository;
import com.group_7.backend.repository.UserMembershipRepository;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.IUserMembershipService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserMembershipServiceImp implements IUserMembershipService {

    private final UserMembershipRepository userMembershipRepository;
    private final UserRepository userRepository;
    private final MembershipPackageRepository membershipPackageRepository;
    private final UserMembershipMapper userMembershipMapper;

    public UserMembershipServiceImp(
            UserMembershipRepository userMembershipRepository,
            UserRepository userRepository,
            MembershipPackageRepository membershipPackageRepository,
            UserMembershipMapper userMembershipMapper
    ) {
        this.userMembershipRepository = userMembershipRepository;
        this.userRepository = userRepository;
        this.membershipPackageRepository = membershipPackageRepository;
        this.userMembershipMapper = userMembershipMapper;
    }

    @Override
    @Transactional
    public UserMembershipDto findOrCreatePendingMembership(UserMembershipDto dto) {
//        // 1. Kiểm tra xem có giao dịch ACTIVE nào không. Nếu có, không cho phép.
//        boolean hasActive = userMembershipRepository.existsByUserUserIdAndStatusIn(dto.getUserId(), List.of(MembershipStatusEnum.ACTIVE));
//        if (hasActive) {
//            throw new IllegalStateException("This user already has an active membership.");
//        }
//
//        // 2. Tìm xem có giao dịch PENDING nào cho gói này không.
//        Optional<UserMembership> existingPending = userMembershipRepository
//                .findByUserUserIdAndMembershipPackageIdAndStatus(dto.getUserId(), dto.getMembershipPackageId(), MembershipStatusEnum.PENDING);
//
//        // 3. Nếu có, trả về chính nó để tái sử dụng
//        if (existingPending.isPresent()) {
//            return userMembershipMapper.toDto(existingPending.get());
//        }

        // 4. Nếu không có, tạo một bản ghi PENDING mới
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + dto.getUserId()));
        MembershipPackage membershipPackage = membershipPackageRepository.findById(dto.getMembershipPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("Package not found: " + dto.getMembershipPackageId()));

        UserMembership newUserMembership = new UserMembership();
        newUserMembership.setUser(user);
        newUserMembership.setMembershipPackage(membershipPackage);
        newUserMembership.setStartDate(LocalDate.now());
        newUserMembership.setPaymentMethod(dto.getPaymentMethod());
        newUserMembership.setStatus(MembershipStatusEnum.ACTIVE);

        if (membershipPackage.getDurationInDays() > 0) {
            LocalDate endDate = newUserMembership.getStartDate().plusDays(membershipPackage.getDurationInDays());
            newUserMembership.setEndDate(endDate);
        }

        UserMembership saved = userMembershipRepository.save(newUserMembership);
        return userMembershipMapper.toDto(saved);
    }


//    @Override
//    @Transactional
//    public UserMembershipDto create(UserMembershipDto dto) {
//        //Kiểm tra user hay gói membership đã có
//        User user = userRepository.findById(dto.getUserId())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
//        MembershipPackage membershipPackage = membershipPackageRepository.findById(dto.getMembershipPackageId())
//                .orElseThrow(() -> new ResourceNotFoundException("MembershipPackage not found with id: " + dto.getMembershipPackageId()));
//        // Nếu user đã có gói ACTIVE, không cho tạo thêm.
//        Optional<UserMembership> activeCheck = userMembershipRepository.findTopByUserUserIdAndStatusOrderByStartDateDesc(dto.getUserId(), MembershipStatusEnum.ACTIVE);
//        if (activeCheck.isPresent()) {
//            throw new IllegalStateException("This user already has an active membership.");
//        }
//
//        UserMembership userMembership = new UserMembership();
//        userMembership.setUser(user);
//        userMembership.setMembershipPackage(membershipPackage);
//        userMembership.setStartDate(LocalDate.now());
//        userMembership.setPaymentMethod(dto.getPaymentMethod());
//        userMembership.setStatus(MembershipStatusEnum.PENDING);
//
//        // Chỉnh ngày hết hạn
//        if (membershipPackage.getDurationInDays() > 0) {
//            LocalDate endDate = userMembership.getStartDate().plusDays(membershipPackage.getDurationInDays());
//            userMembership.setEndDate(endDate);
//        }
//
//        UserMembership saved = userMembershipRepository.save(userMembership);
//        return userMembershipMapper.
//                toDto(saved);
//    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserMembershipDto> getByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return userMembershipRepository.findByUserUserId(user.getUserId())
                .stream()
                .map(userMembershipMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("#userId == authentication.principal.id")
    public UserMembershipDto getCurrentMembership(Long userId) {
        UserMembership current = userMembershipRepository.findTopByUserUserIdAndStatusOrderByStartDateDesc(userId, MembershipStatusEnum.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("There is no active membership found for this user"));
        return userMembershipMapper.toDto(current);
    }

    @Override
    public UserMembershipDto getCurrentMembershipForLogin(Long userId) {
        UserMembership current = userMembershipRepository.findTopByUserUserIdAndStatusOrderByStartDateDesc(userId, MembershipStatusEnum.ACTIVE).orElse(null);
        return userMembershipMapper.toDto(current);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserMembershipDto getById(Long id) {
        UserMembership entity = userMembershipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserMembership not found with id: " + id));
        return userMembershipMapper.toDto(entity);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserMembershipDto> getAll() {
        return userMembershipRepository.findAll()
                .stream()
                .map(userMembershipMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserMembershipDto update(Long id, UserMembershipDto dto) {
        UserMembership entity = userMembershipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserMembership not found with id: " + id));
        entity.setStatus(dto.getStatus());
        UserMembership saved = userMembershipRepository.save(entity);
        return userMembershipMapper.toDto(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(Long id) {
        UserMembership entity = userMembershipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserMembership not found with id: " + id));
        userMembershipRepository.delete(entity);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserMembershipDto updateStatus(Long id, MembershipStatusEnum status) {
        UserMembership entity = userMembershipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserMembership not found with id: " + id));
        entity.setStatus(status);
        UserMembership saved = userMembershipRepository.save(entity);
        return userMembershipMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void updateAllActiveMemberships() {
        LocalDate today = LocalDate.now();
        List<UserMembership> memberships = userMembershipRepository.findAllByStatus(MembershipStatusEnum.ACTIVE);
        for (UserMembership membership : memberships) {
            if (membership.getEndDate() != null && !membership.getEndDate().isAfter(today)) {
                membership.setStatus(MembershipStatusEnum.EXPIRED);
                userMembershipRepository.save(membership);
            }
        }
    }

    @Override
    public UserMembershipDto updateStatusForIpn(Long id, MembershipStatusEnum status) {
        // Logic y hệt, nhưng không có @PreAuthorize
        UserMembership entity = userMembershipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserMembership not found for IPN update with id: " + id));

        // Logic bổ sung: chỉ cập nhật nếu trạng thái đang là PENDING để tránh ghi đè
        if (entity.getStatus() == MembershipStatusEnum.PENDING) {
            entity.setStatus(status);
            // Nếu kích hoạt thành công, cập nhật lại ngày bắt đầu và ngày kết thúc
            if (status == MembershipStatusEnum.ACTIVE) {
                entity.setStartDate(LocalDate.now());
                int duration = entity.getMembershipPackage().getDurationInDays();
                entity.setEndDate(LocalDate.now().plusDays(duration));
            }
            UserMembership saved = userMembershipRepository.save(entity);
            return userMembershipMapper.toDto(saved);
        }
        // Nếu không phải PENDING (ví dụ đã ACTIVE/EXPIRED), thì không làm gì và trả về trạng thái hiện tại
        return userMembershipMapper.toDto(entity);
    }

    @Override
    public UserMembershipDto create(UserMembershipDto t) {
        return null;
    }
}