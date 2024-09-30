package com.se1858.group4.Land_Auction_SWP391.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidResponseDTO {
    private String sender;         // Tên người dùng đặt bid
    private Long content;        // Số tiền bid
    private LocalDateTime bidTime; // Thời điểm đặt bid
    private String type;           // Loại thông điệp (ví dụ: 'BID')
    private int auctionId;         // ID của phiên đấu giá
}
