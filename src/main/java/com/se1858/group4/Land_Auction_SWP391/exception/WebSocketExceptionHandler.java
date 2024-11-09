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
        messagingTemplate.convertAndSendToUser(
                ex.getUsername(),
                "/queue/errors",
                ex.getMessage()
        );
    }
}
