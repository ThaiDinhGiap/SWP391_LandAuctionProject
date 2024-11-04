package com.se1858.group4.Land_Auction_SWP391.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private int notificationId;
    private String content;
    private LocalDateTime createdDate;
    private String readStatus;
    private int auctionId;
}
