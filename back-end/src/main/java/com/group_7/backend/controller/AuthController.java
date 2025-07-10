package com.group_7.backend.controller;

import com.group_7.backend.dto.*;
import com.group_7.backend.dto.request.LoginRequestDto;
import com.group_7.backend.dto.request.RegRequestDto;
import com.group_7.backend.dto.response.JwtResponseDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.IQuitPlanService;
import com.group_7.backend.service.IUserMembershipService;
import com.group_7.backend.service.IUserService;
import com.group_7.backend.util.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final IUserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserMembershipService userMembershipService;
    private final IQuitPlanService quitPlanService;

    public AuthController(IUserService userService, JwtTokenProvider jwtTokenProvider, IUserMembershipService userMembershipService, IQuitPlanService quitPlanService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMembershipService = userMembershipService;
        this.quitPlanService = quitPlanService;
    }

    /*trả về JWT token + user info khi đăng nhập thành công*/
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
        UserDto user = userService.authenticate(request.getUsernameOrEmail(), request.getPassword());
        if (user == null) {
            //Trả về LoginResponse chứa lỗi
            return ResponseEntity.status(401)
                    .body(new ResponseDto("success","Login failed: Invalid credentials"));
        }
        String token= jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());
        UserMembershipDto currentMembership = userMembershipService.getCurrentMembershipForLogin(user.getUserId());
        QuitPlanDto currentQuitPlan = quitPlanService.getCurrentByUserIdAndStatus(user.getUserId());
        return ResponseEntity.ok(
                new JwtResponseDto("success","Login successfully!",token,user, currentMembership, currentQuitPlan));//Trả về JwtResponse chứa token và user info
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@Valid @RequestBody RegRequestDto request) {
        UserDto user = userService.register(request);
        return ResponseEntity.ok(
                new ResponseDto("success", "User registered successfully", user)
        );
    }
}