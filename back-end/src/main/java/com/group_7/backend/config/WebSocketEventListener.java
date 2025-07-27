package com.group_7.backend.config;

import com.group_7.backend.service.OnlineUserService;
import com.group_7.backend.util.JwtTokenProvider;
import com.group_7.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    @Autowired
    private OnlineUserService onlineUserService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private IUserService userService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        // Extract JWT token from headers
        String authToken = headerAccessor.getFirstNativeHeader("Authorization");
        if (authToken != null && authToken.startsWith("Bearer ")) {
            String token = authToken.substring(7);
            try {
                if (jwtTokenProvider.validateToken(token)) {
                    String username = jwtTokenProvider.getUsernameFromJWT(token);
                    Long userId = getUserIdFromUsername(username);
                    
                    if (userId != null) {
                        onlineUserService.addUser(userId);
                        // Store user ID in session attributes for disconnect event
                        headerAccessor.getSessionAttributes().put("userId", userId);
                    }
                }
            } catch (Exception e) {
                // Invalid token, ignore
            }
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
        // Get user ID from session attributes
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        if (userId != null) {
            onlineUserService.removeUser(userId);
        }
    }
    
    private Long getUserIdFromUsername(String username) {
        try {
            return userService.findUserByUsername(username).getUserId();
        } catch (Exception e) {
            return null;
        }
    }
}
