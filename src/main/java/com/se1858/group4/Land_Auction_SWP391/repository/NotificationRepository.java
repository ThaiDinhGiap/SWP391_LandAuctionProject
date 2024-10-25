package com.se1858.group4.Land_Auction_SWP391.repository;

import com.se1858.group4.Land_Auction_SWP391.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query("SELECT n FROM Notification n JOIN n.accounts a WHERE a.accountId = :accountId ORDER BY n.createdDate DESC")
    List<Notification> findByAccountIdOrderByCreatedDateDesc(@Param("accountId") int accountId);
}
