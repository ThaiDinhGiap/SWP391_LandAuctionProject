package com.se1858.group4.Land_Auction_SWP391.repository;

import com.se1858.group4.Land_Auction_SWP391.entity.AuctionSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface AuctionSessionRepository extends JpaRepository<AuctionSession, Integer> {
    @Query("SELECT a FROM AuctionSession a WHERE "
            + "(:keyword IS NULL OR a.auctionName LIKE %:keyword%) AND "
            + "(:fromDate IS NULL OR a.startTime >= :fromDate) AND "
            + "(:toDate IS NULL OR a.startTime <= :toDate) AND "
            + "(:status IS NULL OR a.status LIKE :status)")
    Page<AuctionSession> filterAuctionSessions(
            @Param("keyword") String keyword,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("status") String status,
            Pageable pageable);


    @Query("SELECT a FROM AuctionSession a WHERE a.auctioneer.accountId = :auctioneerId")

    List<AuctionSession> findByAuctioneerId(@Param("auctioneerId") int auctioneerId);

    List<AuctionSession> findByStatus(String status);

    @Query("SELECT a FROM AuctionSession a WHERE a.auctioneer.accountId = :auctioneerId AND LOWER(a.auctionName) LIKE LOWER(CONCAT('%', :auctionName, '%'))")
    List<AuctionSession> findByAuctioneerIdAndAuctionNameContainingIgnoreCase(@Param("auctioneerId") int auctioneerId, @Param("auctionName") String auctionName);

    @Query("SELECT a FROM AuctionSession a WHERE a.auctioneer.accountId = :auctioneerId")
    Page<AuctionSession> findByAuctioneerId(@Param("auctioneerId") int auctioneerId, Pageable pageable);

    @Query("SELECT a FROM AuctionSession a WHERE a.auctioneer.accountId = :auctioneerId AND LOWER(a.auctionName) LIKE LOWER(CONCAT('%', :auctionName, '%'))")
    Page<AuctionSession> findByAuctioneerIdAndAuctionNameContainingIgnoreCase(@Param("auctioneerId") int auctioneerId, @Param("auctionName") String auctionName, Pageable pageable);


}
