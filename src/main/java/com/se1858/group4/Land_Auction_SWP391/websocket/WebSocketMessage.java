package com.se1858.group4.Land_Auction_SWP391.websocket;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage {
    private String content;
    private String status;
    private Integer staffId;
    private Integer clientId;
}
