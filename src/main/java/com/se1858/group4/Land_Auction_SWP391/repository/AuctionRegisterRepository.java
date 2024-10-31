package com.se1858.group4.Land_Auction_SWP391.repository;

import org.springframework.data.jpa.repository.JpaRepository;
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
    List<AuctionRegister> findByBuyer_AccountIdOrderByRegistrationTimeDesc(int accountId);
}