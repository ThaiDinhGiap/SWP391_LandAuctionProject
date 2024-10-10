package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.websocket.WebSocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void requestConfirmationFromStaff(Integer staffId, Integer clientId) {
        // Tạo một thông điệp yêu cầu staff phản hồi
        WebSocketMessage message = new WebSocketMessage();
        message.setContent("Client muốn kết nối với bạn. Bạn có chấp nhận không?");
        message.setStatus("Pending");
        message.setStaffId(staffId);
        message.setClientId(clientId);

        // Gửi yêu cầu tới staff qua WebSocket
        messagingTemplate.convertAndSend("/topic/staff/" + staffId, message);
    }

//    // Lưu phản hồi của staff
//    public void processStaffResponse(Long staffId, boolean isAccepted) {
//        staffResponses.put(staffId, isAccepted);
//    }

    // Gửi thông báo cho client
    public void notifyClient(String status, Integer staffId, Integer clientId) {
        WebSocketMessage responseMessage = new WebSocketMessage();
        responseMessage.setContent("Trạng thái kết nối: " + status);
        responseMessage.setStaffId(staffId);
        responseMessage.setClientId(clientId);
        responseMessage.setStatus(status);

        messagingTemplate.convertAndSend("/topic/client/" + clientId, responseMessage);
    }
}

