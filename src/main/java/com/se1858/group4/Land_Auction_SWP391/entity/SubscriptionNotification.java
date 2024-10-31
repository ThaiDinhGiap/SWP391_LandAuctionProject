package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Subscription_Notification")
public class SubscriptionNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private int subscriptionId;

    @Column(name = "endpoint", nullable = false, length = 512)
    private String endpoint;

    @Column(name = "p256dh", nullable = false, length = 256)
    private String p256dh;

    @Column(name = "auth", nullable = false, length = 256)
    private String auth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Account account;

    public SubscriptionNotification(String endpoint, String p256dh, String auth, Account account) {
        this.endpoint = endpoint;
        this.p256dh = p256dh;
        this.auth = auth;
        this.account = account;
    }
}
