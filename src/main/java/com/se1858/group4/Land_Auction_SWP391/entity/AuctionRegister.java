package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Auction_register")
public class AuctionRegister {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "register_id")
    private int registerId;
	
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "auction_id")
    private AuctionSession auction;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "auctionRegister",
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Bid> bids;

    public AuctionRegister() {
    }

    public AuctionRegister(AuctionSession auction, Account buyer, String registerStatus,
                           String purchaseStatus, String depositStatus, String nickName, LocalDateTime registrationTime) {
		this.auction = auction;
		this.buyer = buyer;
		this.registerStatus = registerStatus;
		this.purchaseStatus = purchaseStatus;
		this.depositStatus = depositStatus;
		this.nickName = nickName;
		this.registrationTime = registrationTime;
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

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }
    
	public int getRegisterId() {
		return registerId;
	}

	public void setRegisterId(int registerId) {
		this.registerId = registerId;
	}

	@Override
	public String toString() {
		return "Auction_register [registerId=" + registerId + ", auction=" + auction + ", buyer=" + buyer
				+ ", registerStatus=" + registerStatus + ", purchaseStatus=" + purchaseStatus + ", depositStatus="
				+ depositStatus + ", nickName=" + nickName + ", registrationTime=" + registrationTime + "]";
	}

}

