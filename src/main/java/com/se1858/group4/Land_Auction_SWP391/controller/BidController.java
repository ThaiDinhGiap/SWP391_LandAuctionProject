package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.BidRequestDTO;
import com.se1858.group4.Land_Auction_SWP391.dto.BidResponseDTO;
import com.se1858.group4.Land_Auction_SWP391.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class BidController {

    @Autowired
    private BidService bidService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/bid")
    public void handleBid(@Payload BidRequestDTO bidRequestDTO) {
        // Xử lý bid
        BidResponseDTO response = bidService.placeBid(bidRequestDTO);

        // Gửi tin nhắn đến topic với auctionId cụ thể
        simpMessagingTemplate.convertAndSend("/topic/auction/" + bidRequestDTO.getAuctionId(), response);
    }
}
