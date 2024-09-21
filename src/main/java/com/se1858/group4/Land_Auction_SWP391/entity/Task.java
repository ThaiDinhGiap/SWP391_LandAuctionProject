package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Task")
public class Task {
    @Id
    @Column(name = "task_id")
    private int taskId;

    @ManyToOne
    @JoinColumn(name = "property_agent_id", nullable = false)
    private Account propertyAgent;

    @ManyToOne
    @JoinColumn(name = "auctioneer_id", nullable = false)
    private Account auctioneer;

    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Column(name = "content_task")
    private String contentTask;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "finished_date")
    private LocalDateTime finishedDate;

    @Column(name = "status")
    private String status;

    public Task() {
    }

    public Task(int taskId, Account propertyAgent, Account auctioneer, Asset asset, String contentTask, LocalDateTime createdDate, LocalDateTime finishedDate, String status) {
        this.taskId = taskId;
        this.propertyAgent = propertyAgent;
        this.auctioneer = auctioneer;
        this.asset = asset;
        this.contentTask = contentTask;
        this.createdDate = createdDate;
        this.finishedDate = finishedDate;
        this.status = status;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Account getPropertyAgent() {
        return propertyAgent;
    }

    public void setPropertyAgent(Account propertyAgent) {
        this.propertyAgent = propertyAgent;
    }

    public Account getAuctioneer() {
        return auctioneer;
    }

    public void setAuctioneer(Account auctioneer) {
        this.auctioneer = auctioneer;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public String getContentTask() {
        return contentTask;
    }

    public void setContentTask(String contentTask) {
        this.contentTask = contentTask;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(LocalDateTime finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", propertyAgent=" + propertyAgent +
                ", auctioneer=" + auctioneer +
                ", asset=" + asset +
                ", contentTask='" + contentTask + '\'' +
                ", createdDate=" + createdDate +
                ", finishedDate=" + finishedDate +
                ", status='" + status + '\'' +
                '}';
    }


}
