package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Notification")
public class Notification {
    @Id
    @Column(name = "notification_id")
    private int notificationId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "read_status")
    private String readStatus;

    public Notification() {
    }

    public Notification(int notificationId, String content, LocalDateTime createdDate, String readStatus) {
        this.notificationId = notificationId;
        this.content = content;
        this.createdDate = createdDate;
        this.readStatus = readStatus;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
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

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
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
}
