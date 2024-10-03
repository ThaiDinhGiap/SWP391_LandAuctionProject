package com.se1858.group4.Land_Auction_SWP391.repository;

import com.se1858.group4.Land_Auction_SWP391.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {

    // Lấy tất cả các bid của một phiên đấu giá theo auctionId
    List<Bid> findByAuctionRegister_Auction_AuctionIdOrderByTimeCreateBidDesc(int auctionId);

    // Lấy tất cả các bid của một người tham gia cụ thể
    List<Bid> findByAuctionRegister_RegisterIdOrderByTimeCreateBidDesc(int registerId);

    // Lấy bid cao nhất của một phiên đấu giá
    Bid findTopByAuctionRegister_Auction_AuctionIdOrderByBidAmountDesc(int auctionId);

    // Đếm tổng số lượng bid trong một phiên đấu giá
    long countByAuctionRegister_Auction_AuctionId(int auctionId);
}
