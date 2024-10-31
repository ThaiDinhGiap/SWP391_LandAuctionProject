package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Bid")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    private int bidId;


    @Column(name = "bid_amount")
    private Long bidAmount;

    @Column(name = "time_create_bid")
    private LocalDateTime timeCreateBid;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "register_id")
    private AuctionRegister auctionRegister;

    public Bid() {
    }

    public Bid(Long bidAmount, AuctionRegister auctionRegister, LocalDateTime timeCreateBid) {
        this.bidAmount = bidAmount;
        this.auctionRegister = auctionRegister;
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

    public AuctionRegister getAuctionRegister() {
        return auctionRegister;
    }

    public void setAuctionRegister(AuctionRegister auctionRegister) {
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
