package com.group_7.backend.service.impl;

import com.group_7.backend.service.IAdminDashboardService;
import com.group_7.backend.entity.enums.MembershipStatusEnum;
import com.group_7.backend.entity.enums.UserRoleEnum;
import com.group_7.backend.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminDashboardService implements IAdminDashboardService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserMembershipRepository userMembershipRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    // PaymentRepository is optional for now

    public AdminDashboardService(
            UserRepository userRepository,
            PostRepository postRepository,
            CommentRepository commentRepository,
            UserMembershipRepository userMembershipRepository,
            AchievementRepository achievementRepository,
            UserAchievementRepository userAchievementRepository
            // PaymentRepository paymentRepository // Not used yet
    ) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userMembershipRepository = userMembershipRepository;
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
        // this.paymentRepository = paymentRepository;
    }

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Users
        long totalUsers = userRepository.count();
        Map<String, Long> usersByRole = new HashMap<>();
        for (UserRoleEnum role : UserRoleEnum.values()) {
            usersByRole.put(role.name(), userRepository.countByRole(role));
        }
        // No lastLogin, so set to 0
        long activeUsersWeek = 0;
        long activeUsersMonth = 0;

        // Posts
        long totalPosts = postRepository.count();
        long postsThisWeek = postRepository.countPostsSince(LocalDateTime.now().minusDays(7));

        // Comments
        long totalComments = commentRepository.count();

        // Memberships
        Map<String, Long> memberships = new HashMap<>();
        memberships.put("active", userMembershipRepository.countByStatusSafe(MembershipStatusEnum.ACTIVE));
        memberships.put("expired", userMembershipRepository.countByStatusSafe(MembershipStatusEnum.EXPIRED));
        memberships.put("paid", userMembershipRepository.countByStatusSafe(MembershipStatusEnum.COMPLETED));

        // Achievements
        List<Map<String, Object>> achievements = achievementRepository.findAll().stream().map(a -> {
            Map<String, Object> ach = new HashMap<>();
            ach.put("name", a.getName());
            ach.put("count", userAchievementRepository.countByAchievementId(a.getAchievementId()));
            return ach;
        }).collect(Collectors.toList());

        // Revenue (mocked for demo)
        BigDecimal totalRevenue = BigDecimal.ZERO;

        // Assemble
        stats.put("totalUsers", totalUsers);
        stats.put("usersByRole", usersByRole);
        stats.put("activeUsersWeek", activeUsersWeek);
        stats.put("activeUsersMonth", activeUsersMonth);
        stats.put("totalPosts", totalPosts);
        stats.put("postsThisWeek", postsThisWeek);
        stats.put("totalComments", totalComments);
        stats.put("memberships", memberships);
        stats.put("achievements", achievements);
        stats.put("totalRevenue", totalRevenue);

        return stats;
    }
}