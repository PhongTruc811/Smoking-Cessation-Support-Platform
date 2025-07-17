package com.group_7.backend.shedule;

import com.group_7.backend.service.ISenderService;
import com.group_7.backend.service.IUserMembershipService;
import com.group_7.backend.service.impl.UserMembershipServiceImp;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class DailyUpdate {


    private final IUserMembershipService userMembershipService;
    private final ISenderService senderService;

    public DailyUpdate(UserMembershipServiceImp userMembershipService, ISenderService senderService) {
        this.userMembershipService = userMembershipService;
        this.senderService = senderService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Mỗi ngày lúc 0h
    public void dailyStatusUpdate() {
        userMembershipService.updateAllActiveMemberships();
        // ... gọi các service khác nếu có
    }

    @Scheduled(cron = "0 0/5 * * * ?") // Mỗi 5 phút
    public void quickStatusUpdate() {
        //userMembershipService.updateAllActiveMemberships();
        String subject = "🌟 Keep Going! Your Smoke-Free Journey Matters";
        //senderService.sendMessage("aquatestsmoking@gmail.com",subject, null);
    }
}
