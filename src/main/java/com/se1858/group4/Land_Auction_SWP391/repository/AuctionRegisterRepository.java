package com.se1858.group4.Land_Auction_SWP391.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.se1858.group4.Land_Auction_SWP391.entity.AuctionRegister;

@Repository
public interface AuctionRegisterRepository extends JpaRepository<AuctionRegister, Integer> {
}