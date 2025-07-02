package com.group_7.backend.shedule;

import com.group_7.backend.service.impl.UserMembershipServiceImp;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyUpdate {


    private final UserMembershipServiceImp userMembershipService;

    public DailyUpdate(UserMembershipServiceImp userMembershipService) {
        this.userMembershipService = userMembershipService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Mỗi ngày lúc 0h
    public void dailyStatusUpdate() {
        userMembershipService.updateAllActiveMemberships();
        // ... gọi các service khác nếu có
    }
}
