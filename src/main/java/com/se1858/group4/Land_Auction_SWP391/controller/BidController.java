package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.BidRequestDTO;
import com.se1858.group4.Land_Auction_SWP391.dto.BidResponseDTO;
import com.se1858.group4.Land_Auction_SWP391.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class BidController {

    @Autowired
    private BidService bidService;

    @MessageMapping("/bid")
    @SendTo("/topic/auction")
    public BidResponseDTO handleBid(BidRequestDTO bidRequestDTO) {
        // Gọi BidService để xử lý logic bid
        return bidService.placeBid(bidRequestDTO);
    }
}