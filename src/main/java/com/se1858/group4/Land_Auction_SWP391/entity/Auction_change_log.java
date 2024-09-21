package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Auction_change_log")
public class Auction_change_log {

    @Id
    @Column(name = "change_id")
    private int changeId;

    @Column(name = "change_type", length = 255)
    private String changeType;

    @Column(name = "reason", columnDefinition = "NVARCHAR(MAX)")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "auction_id")
    private Auction_session auction_session;

    @Column(name = "time")
    private LocalDateTime time;

    public Auction_change_log(Auction_session auction_session, int changeId, String changeType, String reason, LocalDateTime time) {
        this.auction_session = auction_session;
        this.changeId = changeId;
        this.changeType = changeType;
        this.reason = reason;
        this.time = time;
    }

    public Auction_change_log() {
    }

    public int getChangeId() {
        return changeId;
    }

    public void setChangeId(int changeId) {
        this.changeId = changeId;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Auction_session getAuction_session() {
        return auction_session;
    }

    public void setAuction_session(Auction_session auction_session) {
        this.auction_session = auction_session;
    }

    @Override
    public String toString() {
        return "Auction_change_log{" +
                "auction_session=" + auction_session +
                ", changeId=" + changeId +
                ", changeType='" + changeType + '\'' +
                ", reason='" + reason + '\'' +
                ", time=" + time +
                '}';
    }
}
