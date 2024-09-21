package com.se1858.group4.Land_Auction_SWP391.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Ban_log")
public class Ban_log {

    @Id
    @Column(name = "log_id")
    private int logId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "admin_id", referencedColumnName = "account_id")
    private Account admin;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "reason", columnDefinition = "NVARCHAR(MAX)")
    private String reason;

    public Ban_log() {
    }

    public Ban_log(int logId, Account admin, Account account, String reason, LocalDateTime timestamp) {
        this.logId = logId;
        this.admin = admin;
        this.account = account;
        this.reason = reason;
        this.timestamp = timestamp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public Account getAdmin() {
        return admin;
    }

    public void setAdmin(Account admin) {
        this.admin = admin;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Ban_log{" +
                "account=" + account +
                ", logId=" + logId +
                ", admin=" + admin +
                ", timestamp=" + timestamp +
                ", reason='" + reason + '\'' +
                '}';
    }
}
