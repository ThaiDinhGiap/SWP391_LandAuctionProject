package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.service.WebSocketService;
import com.se1858.group4.Land_Auction_SWP391.websocket.WebSocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketChatController {

    private WebSocketService webSocketService;

    @Autowired
    public WebSocketChatController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/staff/confirmConnection")
    public void confirmConnection(WebSocketMessage message) {
        Integer staffId = message.getStaffId();
        Integer clientId = message.getClientId();
        String status = message.getStatus();

        // Xử lý phản hồi của staff: chấp nhận hoặc từ chối
//        webSocketService.processStaffResponse(staffId, isAccepted);

        // Thông báo kết quả cho client
        if (status.equals("Accepted")) {
            webSocketService.notifyClient("accepted", staffId, clientId);
        } else if (status.equals("Rejected")) {
            webSocketService.notifyClient("rejected", staffId, clientId);
        }
    }
}
