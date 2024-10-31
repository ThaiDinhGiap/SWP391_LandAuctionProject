package com.se1858.group4.Land_Auction_SWP391.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndAuctionMessage {
    private int auctionId;
    private String typeMessage; // Completed, Cancelled
    private Long dealPrice;
}
