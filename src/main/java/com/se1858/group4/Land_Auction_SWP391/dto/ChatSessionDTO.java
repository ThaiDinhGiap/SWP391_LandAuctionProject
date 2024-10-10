package com.se1858.group4.Land_Auction_SWP391.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatSessionDTO {
    private Long sessionId;
    private Long customerId;
    private Long staffId;
    private Date startTime;
    private Date endTime;
}
