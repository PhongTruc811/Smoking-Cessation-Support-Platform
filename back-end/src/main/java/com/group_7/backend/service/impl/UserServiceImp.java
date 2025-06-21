package com.group_7.backend.service.impl;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public UserDto getById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
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
    public void delete(Long userId) {
        if (!userRepository.existsById(userId)) throw new ResourceNotFoundException("User not found");
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public UserDto changeStatus(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setStatus(!user.getStatus());
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto authenticate(String usernameOrEmail, String password) {
        Optional<User> userOpt = userRepository.findByUsername(usernameOrEmail);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(usernameOrEmail);
        }
        if (userOpt.isEmpty()) throw new ResourceNotFoundException("User not found");

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
        user.setCreatedAt(LocalDateTime.now());
        //Mặc định role của user mới là MEMBER
        user.setRole(UserRoleEnum.MEMBER);

        // Mã hóa mật khẩu (later)
        // passwordEncoder.encode(request.getPassword());
        user.setPassword(request.getPassword());

        // Lưu DB
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }


}