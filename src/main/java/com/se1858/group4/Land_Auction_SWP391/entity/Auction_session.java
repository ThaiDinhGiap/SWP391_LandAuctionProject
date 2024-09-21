package com.se1858.group4.Land_Auction_SWP391.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Auction_session")
public class Auction_session {

    @Id
    @Column(name = "auction_id")
    private int auctionId;

    @Column(name = "auction_name", length = 255)
    private String auctionName;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "expected_end_time")
    private LocalDateTime expectedEndTime;

    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;

    @Column(name = "starting_price")
    private Long startingPrice;

    @Column(name = "starting_price_per_unit")
    private Long startingPricePerUnit;

    @Column(name = "dealed_price")
    private Long dealedPrice;

    @Column(name = "dealed_price_per_unit")
    private Long dealedPricePerUnit;

    @Column(name = "current_highest_price")
    private Long currentHighestPrice;

    @ManyToOne(fetch = FetchType.LAZY, cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "auctioneer_id", referencedColumnName = "account_id")
    private Account auctioneer;

    @ManyToOne(fetch = FetchType.LAZY, cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "winner_id", referencedColumnName = "account_id")
    private Account winner;

    @Column(name = "minimum_bid_increment")
    private Long minimumBidIncrement;

    @Column(name = "deposit")
    private Long deposit;

    @Column(name = "register_fee")
    private Long registerFee;

    @Column(name = "extra_time_unit")
    private int extraTimeUnit;

    @Column(name = "status", length = 50)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY, cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "asset_id", referencedColumnName = "asset_id")
    private Asset asset;

    @OneToMany(mappedBy = "auction_session", fetch = FetchType.LAZY,
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Auction_change_log> auction_change_logs;

    @OneToMany(mappedBy = "auction", fetch = FetchType.LAZY,
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Auction_register> auction_registers;

    public Auction_session() {
    }

    public Auction_session(int auctionId, String auctionName, Long startingPrice, Long startingPricePerUnit, Account auctioneer, Asset asset, Long minimumBidIncrement, int extraTimeUnit, LocalDateTime startTime, LocalDateTime expectedEndTime, LocalDateTime actualEndTime, Long registerFee, Long currentHighestPrice, Long dealedPrice, Long dealedPricePerUnit, Long deposit, Account winner, String status) {
        this.auctionId = auctionId;
        this.auctionName = auctionName;
        this.startingPrice = startingPrice;
        this.startingPricePerUnit = startingPricePerUnit;
        this.auctioneer = auctioneer;
        this.asset = asset;
        this.minimumBidIncrement = minimumBidIncrement;
        this.extraTimeUnit = extraTimeUnit;
        this.startTime = startTime;
        this.expectedEndTime = expectedEndTime;
        this.actualEndTime = actualEndTime;
        this.registerFee = registerFee;
        this.currentHighestPrice = currentHighestPrice;
        this.dealedPrice = dealedPrice;
        this.dealedPricePerUnit = dealedPricePerUnit;
        this.deposit = deposit;
        this.winner = winner;
        this.status = status;
    }

    public LocalDateTime getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(LocalDateTime actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Account getAuctioneer() {
        return auctioneer;
    }

    public void setAuctioneer(Account auctioneer) {
        this.auctioneer = auctioneer;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public String getAuctionName() {
        return auctionName;
    }

    public void setAuctionName(String auctionName) {
        this.auctionName = auctionName;
    }

    public Long getCurrentHighestPrice() {
        return currentHighestPrice;
    }

    public void setCurrentHighestPrice(Long currentHighestPrice) {
        this.currentHighestPrice = currentHighestPrice;
    }

    public Long getDealedPrice() {
        return dealedPrice;
    }

    public void setDealedPrice(Long dealedPrice) {
        this.dealedPrice = dealedPrice;
    }

    public Long getDealedPricePerUnit() {
        return dealedPricePerUnit;
    }

    public void setDealedPricePerUnit(Long dealedPricePerUnit) {
        this.dealedPricePerUnit = dealedPricePerUnit;
    }

    public Long getDeposit() {
        return deposit;
    }

    public void setDeposit(Long deposit) {
        this.deposit = deposit;
    }

    public LocalDateTime getExpectedEndTime() {
        return expectedEndTime;
    }

    public void setExpectedEndTime(LocalDateTime expectedEndTime) {
        this.expectedEndTime = expectedEndTime;
    }

    public int getExtraTimeUnit() {
        return extraTimeUnit;
    }

    public void setExtraTimeUnit(int extraTimeUnit) {
        this.extraTimeUnit = extraTimeUnit;
    }

    public Long getMinimumBidIncrement() {
        return minimumBidIncrement;
    }

    public void setMinimumBidIncrement(Long minimumBidIncrement) {
        this.minimumBidIncrement = minimumBidIncrement;
    }

    public Long getRegisterFee() {
        return registerFee;
    }

    public void setRegisterFee(Long registerFee) {
        this.registerFee = registerFee;
    }

    public Long getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Long startingPrice) {
        this.startingPrice = startingPrice;
    }

    public Long getStartingPricePerUnit() {
        return startingPricePerUnit;
    }

    public void setStartingPricePerUnit(Long startingPricePerUnit) {
        this.startingPricePerUnit = startingPricePerUnit;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Account getWinner() {
        return winner;
    }

    public void setWinner(Account winner) {
        this.winner = winner;
    }

    public List<Auction_change_log> getAuction_change_logs() {
        return auction_change_logs;
    }

    public void setAuction_change_logs(List<Auction_change_log> auction_change_logs) {
        this.auction_change_logs = auction_change_logs;
    }

    public List<Auction_register> getAuction_registers() {
        return auction_registers;
    }

    public void setAuction_registers(List<Auction_register> auction_registers) {
        this.auction_registers = auction_registers;
    }

    @Override
    public String toString() {
        return "Auction_session{" +
                "actualEndTime=" + actualEndTime +
                ", auctionId=" + auctionId +
                ", auctionName='" + auctionName + '\'' +
                ", startTime=" + startTime +
                ", expectedEndTime=" + expectedEndTime +
                ", startingPrice=" + startingPrice +
                ", startingPricePerUnit=" + startingPricePerUnit +
                ", dealedPrice=" + dealedPrice +
                ", dealedPricePerUnit=" + dealedPricePerUnit +
                ", currentHighestPrice=" + currentHighestPrice +
                ", winner=" + winner +
                ", minimumBidIncrement=" + minimumBidIncrement +
                ", deposit=" + deposit +
                ", registerFee=" + registerFee +
                ", extraTimeUnit=" + extraTimeUnit +
                ", status='" + status + '\'' +
                ", auctioneer=" + auctioneer +
                ", asset=" + asset +
                '}';
    }
}
