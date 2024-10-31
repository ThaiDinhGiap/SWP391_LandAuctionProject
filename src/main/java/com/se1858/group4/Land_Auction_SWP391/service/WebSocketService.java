package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.websocket.ChatMessage;
import com.se1858.group4.Land_Auction_SWP391.websocket.ConnectMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final FirebaseChatService firebaseChatService;

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate, FirebaseChatService firebaseChatService) {
        this.messagingTemplate = messagingTemplate;
        this.firebaseChatService = firebaseChatService;
    }

    public void notifyStaff(ConnectMessage requestMessage) {
        // Gửi yêu cầu tới staff qua WebSocket
        String sessionId = UUID.randomUUID().toString();
        requestMessage.setContent(sessionId);
        messagingTemplate.convertAndSend("/topic/staff/" + requestMessage.getStaffId(), requestMessage);
    }

    // Gửi thông báo cho client
    public void notifyClient(ConnectMessage responseMessage) {
        firebaseChatService.createChatSession(responseMessage.getContent(), responseMessage.getClientId(), responseMessage.getStaffId());
        messagingTemplate.convertAndSend("/topic/client/" + responseMessage.getClientId(), responseMessage);
    }

    // Gửi tin nhắn đến topic tương ứng với sessionId
    public void sendMessageToSession(ChatMessage message) {
        firebaseChatService.saveChatMessage(message.getSessionId(), message.getSender(), message.getContent());
        messagingTemplate.convertAndSend("/topic/chat/" + message.getSessionId(), message);
    }

    public void endChatSession(ChatMessage message) {
        message.setSender("Server");
        messagingTemplate.convertAndSend("/topic/chat/" + message.getSessionId(), message);
    }
}

