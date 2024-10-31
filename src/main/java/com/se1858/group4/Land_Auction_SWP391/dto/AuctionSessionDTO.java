package com.se1858.group4.Land_Auction_SWP391.dto;

import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.entity.AuctionSession;
import com.se1858.group4.Land_Auction_SWP391.entity.Task;

public class AuctionSessionDTO {
    private AuctionSession auctionSession;
    private int taskId;
    private int assetId;
    public AuctionSessionDTO() {}
    public AuctionSessionDTO(AuctionSession auctionSession, int taskId, int assetId) {
        this.auctionSession = auctionSession;
        this.taskId = taskId;
        this.assetId = assetId;
    }

    public AuctionSession getAuctionSession() {
        return auctionSession;
    }

    public void setAuctionSession(AuctionSession auctionSession) {
        this.auctionSession = auctionSession;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
