package com.group_7.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group_7.backend.dto.QuitPlanDto;
import com.group_7.backend.dto.UserDto;
import com.group_7.backend.dto.UserMembershipDto;
import com.group_7.backend.dto.response.JwtResponseDto;
import com.group_7.backend.entity.User;
import com.group_7.backend.entity.enums.UserGenderEnum;
import com.group_7.backend.entity.enums.UserRoleEnum;
import com.group_7.backend.mapper.UserMapper;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.IQuitPlanService;
import com.group_7.backend.service.IUserMembershipService;
import com.group_7.backend.util.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IUserMembershipService userMembershipService;

    @Autowired
    private IQuitPlanService quitPlanService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        // Kiểm tra user đã tồn tại chưa
        Optional<User> userOpt = userRepository.findByEmail(email);

        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            System.out.println(oAuth2User);
            // Lần đầu đăng nhập - lưu vào DB
            user = new User();
            user.setUsername(oAuth2User.getAttribute("name"));
            user.setEmail(oAuth2User.getAttribute("email"));
            user.setFullName(oAuth2User.getAttribute("name"));
            user.setDob(LocalDate.of(2000, 1, 1));
            user.setGender(UserGenderEnum.OTHER);
            user.setStatus(true);
            user.setRole(UserRoleEnum.MEMBER);
            userRepository.save(user);
        }

        // Tạo JWT token
        String jwtToken = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());
        UserDto loginUser = userMapper.toDto(userRepository.findByEmail(user.getEmail()).get());
        UserMembershipDto userMembershipDto = userMembershipService.getCurrentMembershipForLogin(user.getUserId());
        // Trả JWT về FE, hiển thị form json như khi Login bình thường
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        JwtResponseDto jwtResponseDto = new JwtResponseDto(
                "success",
                "Login with Google successfully!",
                jwtToken,
                loginUser,
                userMembershipDto,
                quitPlanService.getCurrentByUserIdAndStatus(user.getUserId())
        );

        String jsonData = objectMapper.writeValueAsString(jwtResponseDto);

        // Encode JSON để truyền an toàn qua URL
        String encodedData = URLEncoder.encode(jsonData, StandardCharsets.UTF_8);
        //String redirectUrl = "http://localhost:5173/oauth2-redirect?token=" + jwtToken;
        String redirectUrl = "https://anti-smoking-fe.vercel.app/oauth2-redirect?token=" + jwtToken;

        response.sendRedirect(redirectUrl);
    }
}

