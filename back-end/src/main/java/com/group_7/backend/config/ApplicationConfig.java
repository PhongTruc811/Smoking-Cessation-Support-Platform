package com.group_7.backend.config;

import com.group_7.backend.entity.User;
import com.group_7.backend.entity.enums.UserGenderEnum;
import com.group_7.backend.entity.enums.UserRoleEnum;
import com.group_7.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
@Slf4j
public class ApplicationConfig {

    @Bean
    ApplicationRunner init(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                User user = new User();
                user.setUsername("admin");
                user.setPassword("admin");
                user.setFullName("Admin");
                user.setGender(UserGenderEnum.MALE);
                user.setEmail("admintesting@gmail.com");
                Set<String> roles = new HashSet<>();
                roles.add(UserRoleEnum.ADMIN.name());
                user.setRoles(roles);

                userRepository.save(user);
                log.warn("Admin user added with default password");
            }
        };
    }
}
