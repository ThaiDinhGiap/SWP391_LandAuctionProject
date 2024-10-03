package com.se1858.group4.Land_Auction_SWP391.repository;

import com.se1858.group4.Land_Auction_SWP391.entity.AuctionChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionChangeLogRepository extends JpaRepository<AuctionChangeLog, Integer> {
}
