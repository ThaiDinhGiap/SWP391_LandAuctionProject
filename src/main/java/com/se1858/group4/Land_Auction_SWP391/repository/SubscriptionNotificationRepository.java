package com.se1858.group4.Land_Auction_SWP391.repository;

import com.se1858.group4.Land_Auction_SWP391.entity.SubscriptionNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionNotificationRepository extends JpaRepository<SubscriptionNotification, Integer> {

    // Lấy danh sách subscription của một account
    List<SubscriptionNotification> findByAccountAccountId(int accountId);

    // Tìm subscription theo endpoint
    Optional<SubscriptionNotification> findByEndpoint(String endpoint);

    // Xóa subscription theo endpoint
    void deleteByEndpoint(String endpoint);
}
