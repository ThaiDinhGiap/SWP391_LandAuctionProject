package com.se1858.group4.Land_Auction_SWP391.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequest {
    private String endpoint;
    private String p256dh;
    private String auth;
}
