package com.se1858.group4.Land_Auction_SWP391.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private int notificationId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "read_status")
    private String readStatus;

    @ManyToMany(mappedBy = "notifications", fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    private List<Account> accounts;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "auction_id")
    private AuctionSession auction;

    public Notification() {
    }

    public Notification(List<Account> accounts, String content, LocalDateTime createdDate, String readStatus) {
        this.accounts = accounts;
        this.content = content;
        this.createdDate = createdDate;
        this.readStatus = readStatus;
    }

    public Notification(int notificationId, String content, LocalDateTime createdDate, String readStatus, List<Account> accounts, AuctionSession auction) {
        this.notificationId = notificationId;
        this.content = content;
        this.createdDate = createdDate;
        this.readStatus = readStatus;
        this.accounts = accounts;
        this.auction = auction;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public AuctionSession getAuction() {
        return auction;
    }

    public void setAuction(AuctionSession auction) {
        this.auction = auction;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                ", readStatus='" + readStatus + '\'' +
                '}';
    }

    public void addAccount(Account account) {
        if (this.accounts == null) {
            this.accounts = new ArrayList<>();
        }
        this.accounts.add(account);
    }
}
