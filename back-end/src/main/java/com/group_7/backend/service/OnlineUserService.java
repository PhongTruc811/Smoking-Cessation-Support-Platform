package com.group_7.backend.service;

import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OnlineUserService {
    
    private final Set<Long> onlineUsers = ConcurrentHashMap.newKeySet();
    
    public void addUser(Long userId) {
        onlineUsers.add(userId);
    }
    
    public void removeUser(Long userId) {
        onlineUsers.remove(userId);
    }
    
    public Set<Long> getOnlineUsers() {
        return Set.copyOf(onlineUsers);
    }
    
    public boolean isUserOnline(Long userId) {
        return onlineUsers.contains(userId);
    }
    
    public int getOnlineUserCount() {
        return onlineUsers.size();
    }
}
