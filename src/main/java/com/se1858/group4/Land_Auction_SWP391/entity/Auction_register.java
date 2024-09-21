package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Auction_register")
public class Auction_register {
    @Id
    @ManyToOne
    @JoinColumn(name = "auction_id")
    private AuctionSession auction;

    @Id
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Account buyer;

    @Column(name = "result")
    private String result;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "register_status")
    private String registerStatus;

    @Column(name = "purchase_status")
    private String purchaseStatus;

    @Column(name = "deposit_status")
    private String depositStatus;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "registration_time")
    private LocalDateTime registrationTime;

    @Column(name = "registration_open_date")
    private LocalDateTime registrationOpenDate;

    @Column(name = "registration_close_date")
    private LocalDateTime registrationCloseDate;

    public Auction_register() {
    }

    public Auction_register(AuctionSession auction, Account buyer, String result, Integer rank, String registerStatus, String purchaseStatus, String depositStatus, String nickName, LocalDateTime registrationTime, LocalDateTime registrationOpenDate, LocalDateTime registrationCloseDate) {
        this.auction = auction;
        this.buyer = buyer;
        this.result = result;
        this.rank = rank;
        this.registerStatus = registerStatus;
        this.purchaseStatus = purchaseStatus;
        this.depositStatus = depositStatus;
        this.nickName = nickName;
        this.registrationTime = registrationTime;
        this.registrationOpenDate = registrationOpenDate;
        this.registrationCloseDate = registrationCloseDate;
    }

    public AuctionSession getAuction() {
        return auction;
    }

    public void setAuction(AuctionSession auction) {
        this.auction = auction;
    }

    public Account getBuyer() {
        return buyer;
    }

    public void setBuyer(Account buyer) {
        this.buyer = buyer;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }

    public String getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(String purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    public String getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(String depositStatus) {
        this.depositStatus = depositStatus;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public LocalDateTime getRegistrationOpenDate() {
        return registrationOpenDate;
    }

    public void setRegistrationOpenDate(LocalDateTime registrationOpenDate) {
        this.registrationOpenDate = registrationOpenDate;
    }

    public LocalDateTime getRegistrationCloseDate() {
        return registrationCloseDate;
    }

    public void setRegistrationCloseDate(LocalDateTime registrationCloseDate) {
        this.registrationCloseDate = registrationCloseDate;
    }

    @Override
    public String toString() {
        return "Auction_register{" +
                "auction=" + auction +
                ", buyer=" + buyer +
                ", result='" + result + '\'' +
                ", rank=" + rank +
                ", registerStatus='" + registerStatus + '\'' +
                ", purchaseStatus='" + purchaseStatus + '\'' +
                ", depositStatus='" + depositStatus + '\'' +
                ", nickName='" + nickName + '\'' +
                ", registrationTime=" + registrationTime +
                ", registrationOpenDate=" + registrationOpenDate +
                ", registrationCloseDate=" + registrationCloseDate +
                '}';
    }
}

