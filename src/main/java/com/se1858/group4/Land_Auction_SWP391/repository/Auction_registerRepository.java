package com.se1858.group4.Land_Auction_SWP391.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.se1858.group4.Land_Auction_SWP391.entity.Auction_register;

import java.util.List;
import java.util.Optional;

@Repository
public interface Auction_registerRepository extends JpaRepository<Auction_register, Integer> {

    // Lấy danh sách những người đăng ký tham gia phiên đấu giá dựa trên auctionId
    List<Auction_register> findByAuctionSession_AuctionId(int auctionId);

    // Tìm một người đăng ký dựa trên accountId và auctionId (người dùng cụ thể đã đăng ký vào một phiên đấu giá cụ thể)
    Optional<Auction_register> findByAccountIdAndAuctionSession_AuctionId(int accountId, int auctionId);

    // Kiểm tra xem một người dùng đã đăng ký tham gia phiên đấu giá hay chưa
    boolean existsByAccountIdAndAuctionSession_AuctionId(int accountId, int auctionId);

    // Lấy danh sách tất cả phiên đấu giá mà người dùng đã đăng ký
    List<Auction_register> findByAccountId(int accountId);

    // Lấy danh sách những người đăng ký có trạng thái được xác nhận trong một phiên đấu giá
    List<Auction_register> findByAuctionSession_AuctionIdAndStatus(int auctionId, String status);
}
