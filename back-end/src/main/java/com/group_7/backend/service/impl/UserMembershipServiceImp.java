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
    public UserMembershipDto create(UserMembershipDto dto) {
        //Kiểm tra user hay gói membership đã có
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        MembershipPackage membershipPackage = membershipPackageRepository.findById(dto.getMembershipPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("MembershipPackage not found with id: " + dto.getMembershipPackageId()));
        //Kiểm tra user đã có gói membership kích hoạt
        Optional<UserMembership> checkUser = userMembershipRepository.findTopByUserUserIdAndStatusOrderByStartDateDesc(dto.getUserId(), MembershipStatusEnum.ACTIVE);
        if (checkUser!=null) throw new IllegalArgumentException("This user currently have active membership");

        UserMembership userMembership = new UserMembership();
        userMembership.setUser(user);
        userMembership.setMembershipPackage(membershipPackage);
        userMembership.setStartDate(LocalDate.now());
        userMembership.setPaymentMethod(dto.getPaymentMethod());
        userMembership.setStatus(MembershipStatusEnum.PENDING);

        // Chỉnh ngày hết hạn
        if (membershipPackage.getDurationInDays() > 0) {
            LocalDate endDate = userMembership.getStartDate().plusDays(membershipPackage.getDurationInDays());
            userMembership.setEndDate(endDate);
        }

        UserMembership saved = userMembershipRepository.save(userMembership);
        return userMembershipMapper.
                toDto(saved);
    }

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
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
    @Transactional
    public UserMembership createPendingMembership(UserMembershipDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        MembershipPackage membershipPackage = membershipPackageRepository.findById(dto.getMembershipPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("MembershipPackage not found with id: " + dto.getMembershipPackageId()));

        // Kiểm tra user đã có gói membership ACTIVE hay chưa
        // Bạn có thể giữ lại hoặc bỏ qua kiểm tra này trong luồng thanh toán tùy theo nghiệp vụ
        // Ví dụ: có cho phép mua trước gói mới khi gói cũ sắp hết hạn không?
        // Tạm thời tôi vẫn giữ lại.
        Optional<UserMembership> checkUser = userMembershipRepository.findTopByUserUserIdAndStatusOrderByStartDateDesc(dto.getUserId(), MembershipStatusEnum.ACTIVE);
        if (checkUser.isPresent()) {
            throw new IllegalArgumentException("This user currently has an active membership.");
        }

        UserMembership userMembership = new UserMembership();
        userMembership.setUser(user);
        userMembership.setMembershipPackage(membershipPackage);
        userMembership.setStartDate(LocalDate.now());
        userMembership.setPaymentMethod(dto.getPaymentMethod());
        userMembership.setStatus(MembershipStatusEnum.PENDING); // Quan trọng nhất

        if (membershipPackage.getDurationInDays() > 0) {
            LocalDate endDate = userMembership.getStartDate().plusDays(membershipPackage.getDurationInDays());
            userMembership.setEndDate(endDate);
        }

        return userMembershipRepository.save(userMembership);
        // Trả về đối tượng Entity
        }

    @Override
    public UserMembership findEntityById(Long id) {
        return userMembershipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserMembership not found with id: " + id));
    }
}