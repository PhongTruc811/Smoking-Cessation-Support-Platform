package com.group_7.backend.service.impl;

import com.group_7.backend.dto.RegRequestDto;
import com.group_7.backend.dto.UserDto;
import com.group_7.backend.entity.User;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.mapper.UserMapper;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDTO, String plainPassword) {
        if (userRepository.existsByUsername(userDTO.getUsername()) || userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Username or Email already exists");
        }
        User user = UserMapper.toEntity(userDTO);
        user.setPassword(plainPassword);
        user = userRepository.save(user);
        return UserMapper.toDTO(user);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserMapper.toDTO(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(Integer userId, UserDto userDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setFullName(userDTO.getFullName());
        user.setDob(userDTO.getDob());
        user.setGender(userDTO.getGender());
        user.setStatus(userDTO.getStatus());
        // Không update username/email ở đây (thường), có thể bổ sung nếu muốn
        user = userRepository.save(user);
        return UserMapper.toDTO(user);
    }

    @Override
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) throw new ResourceNotFoundException("User not found");
        userRepository.deleteById(userId);
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
        return UserMapper.toDTO(user);
    }

    @Override
    public UserDto register(RegRequestDto request) {
        // Kiểm tra duplicate username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại");
        }
        // Kiểm tra duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng");
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
        user.setRole("MEMBER");

        // Mã hóa mật khẩu (later)
        // passwordEncoder.encode(request.getPassword());
        user.setPassword(request.getPassword());

        // Lưu DB
        User savedUser = userRepository.save(user);

        return UserMapper.toDTO(savedUser);
    }
}
