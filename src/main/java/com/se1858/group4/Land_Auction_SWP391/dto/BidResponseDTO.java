package com.se1858.group4.Land_Auction_SWP391.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidResponseDTO {

    private int bidId;                 // ID của bid
    private Long bidAmount;            // Số tiền bid
    private int registerId;            // ID của người đăng ký tham gia đấu giá
    private LocalDateTime bidTime;     // Thời điểm đặt bid
}