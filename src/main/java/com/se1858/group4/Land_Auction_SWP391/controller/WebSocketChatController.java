package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.service.StaffService;
import com.se1858.group4.Land_Auction_SWP391.service.WebSocketService;
import com.se1858.group4.Land_Auction_SWP391.websocket.ChatMessage;
import com.se1858.group4.Land_Auction_SWP391.websocket.ConnectMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
public class WebSocketChatController {

    private WebSocketService webSocketService;
    private final StaffService staffService;

    @Autowired
    public WebSocketChatController(WebSocketService webSocketService, StaffService staffService) {
        this.webSocketService = webSocketService;
        this.staffService = staffService;
    }

    @MessageMapping("/staff/confirmConnection")
    public void confirmConnection(ConnectMessage message) {
        // Thông báo kết quả cho client
        staffService.setStaffAvailability(message.getStaffId(), true);
        webSocketService.notifyClient(message);
    }

    // Xử lý tin nhắn chat từ client hoặc staff
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage message) {
        // Gửi tin nhắn đến topic tương ứng với sessionId
        webSocketService.sendMessageToSession(message);
    }

    @MessageMapping("/chat.cancelSession")
    public void cancelSession(@Payload ChatMessage message) {
        // Gửi tin nhắn đến topic tương ứng với sessionId
        webSocketService.endChatSession(message);
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@RequestBody Map<String, Object> request, SimpMessageHeaderAccessor headerAccessor) {
        Integer staffId = (Integer) request.get("staffId");

        System.out.println(staffId);

        // Lưu staffId vào session attributes
        headerAccessor.getSessionAttributes().put("staffId", staffId);

        System.out.println(111111);

        // Đánh dấu staff là "available"
        staffService.setStaffAvailability(staffId, true);
    }
}
