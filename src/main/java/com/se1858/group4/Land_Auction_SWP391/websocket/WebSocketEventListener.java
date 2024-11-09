package com.se1858.group4.Land_Auction_SWP391.websocket;

import com.se1858.group4.Land_Auction_SWP391.service.StaffService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private final StaffService staffService;

    public WebSocketEventListener(StaffService staffService) {
        this.staffService = staffService;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Integer staffId = (Integer) headerAccessor.getSessionAttributes().get("staffId");

        if (staffId != null) {
            // Cập nhật trạng thái của staff là offline
            staffService.setStaffAvailability(staffId, false);
        }
    }
}

