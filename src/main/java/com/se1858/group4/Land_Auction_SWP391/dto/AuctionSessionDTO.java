package com.se1858.group4.Land_Auction_SWP391.dto;

import com.se1858.group4.Land_Auction_SWP391.entity.AuctionSession;

public class AuctionSessionDTO {
    private AuctionSession auctionSession;
    public AuctionSessionDTO() {}
    public AuctionSessionDTO(AuctionSession auctionSession) {
        this.auctionSession = auctionSession;
    }

    public AuctionSession getAuctionSession() {
        return auctionSession;
    }

    public void setAuctionSession(AuctionSession auctionSession) {
        this.auctionSession = auctionSession;
    }
}
