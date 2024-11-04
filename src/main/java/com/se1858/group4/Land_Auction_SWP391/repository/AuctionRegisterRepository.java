package com.se1858.group4.Land_Auction_SWP391.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.se1858.group4.Land_Auction_SWP391.entity.AuctionRegister;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRegisterRepository extends JpaRepository<AuctionRegister, Integer> {

    // Kiểm tra xem người dùng có trạng thái "accepted" trong phiên đấu giá không
    Optional<AuctionRegister> findByAuction_AuctionIdAndBuyer_AccountIdAndRegisterStatus(int auctionId, int accountId, String registerStatus);

    Optional<AuctionRegister> findByAuction_AuctionIdAndBuyer_AccountId(int auctionId, int accountId);

    List<AuctionRegister> findByAuction_AuctionId(int auctionId);

    List<AuctionRegister> findByAuction_AuctionIdOrderByRank(int auctionId);

    @Query("SELECT ar FROM AuctionRegister ar LEFT JOIN FETCH ar.bids WHERE ar.auction.auctionId = :auctionId")
    List<AuctionRegister> findByAuction_AuctionIdWithBids(@Param("auctionId") int auctionId);

    @Query("SELECT ar FROM AuctionRegister ar LEFT JOIN FETCH ar.buyer b LEFT JOIN FETCH b.notifications WHERE ar.auction.auctionId = :auctionId")
    List<AuctionRegister> findByAuctionIdWithNotifications(@Param("auctionId") int auctionId);
    List<AuctionRegister> findByBuyer_AccountIdOrderByRegistrationTimeDesc(int accountId);
}