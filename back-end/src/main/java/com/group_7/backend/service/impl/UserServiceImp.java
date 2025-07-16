package com.group_7.backend.service.impl;

import com.group_7.backend.dto.request.PasswordRequestDto;
import com.group_7.backend.dto.request.RegRequestDto;
import com.group_7.backend.dto.UserDto;
import com.group_7.backend.dto.request.UserRequestDto;
import com.group_7.backend.entity.User;
import com.group_7.backend.entity.enums.UserRoleEnum;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.UserMapper;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.IUserService;
import com.group_7.backend.util.JwtTokenProvider;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #userId == authentication.principal.id")//Quyền cho admin hoặc chính user
    public UserDto getById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllByRole(UserRoleEnum role) {
        return userRepository.findByRole(role)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.id")
    public UserDto update(Long userId, UserRequestDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());
        user.setDob(userDto.getDob());
        user.setGender(userDto.getGender());
        // Không update username/email ở đây (thường), có thể bổ sung nếu muốn
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void delete(Long userId) {
        if (!userRepository.existsById(userId)) throw new ResourceNotFoundException("User not found");
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserDto changeStatus(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setStatus(!user.isStatus());
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.id")
    public void changePassword(Long userId, PasswordRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getPassword().equals(requestDto.getCurrentPassword())) {
            user.setPassword(requestDto.getNewPassword());
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Wrong password");
        }
    }

    @Override
    public UserDto authenticate(String usernameOrEmail, String password) {
        Optional<User> userOpt = userRepository.findByUsername(usernameOrEmail);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(usernameOrEmail);
        }
        if (userOpt.isEmpty()) throw new ResourceNotFoundException("User does not exist, please check username/email before login ");

        User user = userOpt.get();

        if (!password.matches(user.getPassword())) {
            throw new IllegalArgumentException("Invalid username/email or password");
        }
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto register(RegRequestDto request) {
        // Kiểm tra duplicate username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        // Kiểm tra duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Tạo entity User
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setDob(request.getDob());
        user.setGender(request.getGender());
        user.setStatus(true);
        user.setCreatedAt(LocalDate.now());
        //Mặc định role của user mới là MEMBER
        user.setRole(UserRoleEnum.MEMBER);
        // Mã hóa mật khẩu (later)
        // passwordEncoder.encode(request.getPassword());
        user.setPassword(request.getPassword());

        // Lưu DB
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

}