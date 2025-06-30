package com.group_7.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group_7.backend.dto.UserDto;
import com.group_7.backend.dto.response.JwtResponseDto;
import com.group_7.backend.entity.User;
import com.group_7.backend.entity.enums.UserRoleEnum;
import com.group_7.backend.mapper.UserMapper;
import com.group_7.backend.repository.UserRepository;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String name = oAuth2User.getAttribute("name");

        // Kiểm tra user đã tồn tại chưa
        Optional<User> userOpt = userRepository.findByUsername(name);
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

            user.setStatus(true);
            user.setRole(UserRoleEnum.MEMBER);
            userRepository.save(user);
        }

        // Tạo JWT token
        String jwtToken = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());
        UserDto newUser = userMapper.toDto(userRepository.findByUsername(user.getUsername()).get());
        // Trả JWT về FE, hiển thị form json như khi Login bình thường
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getWriter(), new JwtResponseDto("success","Login with Google successfully!", jwtToken, newUser));

    }
}

