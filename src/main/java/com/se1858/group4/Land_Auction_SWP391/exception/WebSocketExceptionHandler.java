package com.se1858.group4.Land_Auction_SWP391.exception;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class WebSocketExceptionHandler {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketExceptionHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageExceptionHandler(BidAmountInsufficientException.class)
    public void handleBidAmountInsufficientException(BidAmountInsufficientException ex) {
        // Chuyển đổi thông báo lỗi đến user dựa trên username của họ qua queue cá nhân
        messagingTemplate.convertAndSendToUser(
                ex.getUsername(),          // Lấy username từ exception
                "/queue/errors",           // destination riêng cho người dùng
                ex.getMessage()             // nội dung thông báo lỗi
        );
    }
}
