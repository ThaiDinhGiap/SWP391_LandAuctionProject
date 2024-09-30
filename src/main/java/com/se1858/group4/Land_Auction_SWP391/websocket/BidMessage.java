package com.se1858.group4.Land_Auction_SWP391.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidMessage {
    private int auctionId;
    private String sender;
    private Long bidAmount;
    private String messageType; // Có thể là JOIN, BID, hoặc LEAVE
}

