package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@IdClass(Auction_register.AuctionRegisterId.class)
@Table(name = "Auction_register")
public class Auction_register {
    @Id
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "auction_id")
    private Auction_session auction;

    @Id
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "auctionRegister",
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Bid> bids;

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

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
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

    public static class AuctionRegisterId implements Serializable {
        private Long auction;
        private Long buyer;

        public AuctionRegisterId() {
        }

        public AuctionRegisterId(Long auction, Long buyer) {
            this.auction = auction;
            this.buyer = buyer;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AuctionRegisterId that = (AuctionRegisterId) o;
            return Objects.equals(auction, that.auction) && Objects.equals(buyer, that.buyer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(auction, buyer);
        }
    }
}

