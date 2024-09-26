package com.se1858.group4.Land_Auction_SWP391.repository;

import com.se1858.group4.Land_Auction_SWP391.entity.Auction_session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Auction_sessionRepository extends JpaRepository<Auction_session, Integer> {

    // Lấy danh sách các phiên đấu giá đang diễn ra
    List<Auction_session> findByStatus(String status);

    // Lấy tất cả các phiên đấu giá đã kết thúc
    List<Auction_session> findByStatusOrderByActualEndTimeDesc(String status);

    // Tìm phiên đấu giá theo tên
    Optional<Auction_session> findByAuctionName(String auctionName);

    // Lấy tất cả các phiên đấu giá của một người điều hành cụ thể
    List<Auction_session> findByAuctioneerId(int auctioneerId);

    // Lấy phiên đấu giá tiếp theo dựa trên thời gian bắt đầu
    List<Auction_session> findByStartTimeAfterOrderByStartTimeAsc(java.time.LocalDateTime startTime);

    // Kiểm tra phiên đấu giá tồn tại theo assetId
    boolean existsByAssetId(int assetId);
}
