package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Bid")
public class Bid {

    @Id
    @Column(name = "bid_id")
    private int bidId;

    @Column(name = "bid_amount")
    private Long bidAmount;

    @Column(name = "time_create_bid")
    private LocalDateTime timeCreateBid;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumns({
            @JoinColumn(name = "auction_id", referencedColumnName = "auction_id"),
            @JoinColumn(name = "bidder_id", referencedColumnName = "buyer_id")
    })
    private Auction_register auctionRegister;

    public Bid() {
    }

    public Bid(int bidId, Long bidAmount, Auction_register auctionRegister, LocalDateTime timeCreateBid) {
        this.bidId = bidId;
        this.bidAmount = bidAmount;
        this.Auction_register = auctionRegister;
        this.timeCreateBid = timeCreateBid;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public Long getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(Long bidAmount) {
        this.bidAmount = bidAmount;
    }

    public Auction_register getAuctionRegister() {
        return auctionRegister;
    }

    public void setAuctionRegister(Auction_register auctionRegister) {
        this.auctionRegister = auctionRegister;
    }

    public LocalDateTime getTimeCreateBid() {
        return timeCreateBid;
    }

    public void setTimeCreateBid(LocalDateTime timeCreateBid) {
        this.timeCreateBid = timeCreateBid;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "auctionRegister=" + auctionRegister +
                ", bidId=" + bidId +
                ", bidAmount=" + bidAmount +
                ", timeCreateBid=" + timeCreateBid +
                '}';
    }
}
