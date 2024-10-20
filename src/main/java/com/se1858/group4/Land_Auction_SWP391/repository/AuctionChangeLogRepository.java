package com.se1858.group4.Land_Auction_SWP391.repository;

import com.se1858.group4.Land_Auction_SWP391.entity.AuctionChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionChangeLogRepository extends JpaRepository<AuctionChangeLog, Integer> {
    @Query("SELECT a FROM AuctionChangeLog a WHERE a.auction_session.auctionId = :auctionId")
    List<AuctionChangeLog> findByAuctionId(@Param("auctionId") int auctionId);
}
