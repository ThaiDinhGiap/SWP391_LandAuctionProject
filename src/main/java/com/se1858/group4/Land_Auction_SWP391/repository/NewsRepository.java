package com.se1858.group4.Land_Auction_SWP391.repository;

import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.entity.AuctionRegister;
import com.se1858.group4.Land_Auction_SWP391.entity.LocalAuthority;
import com.se1858.group4.Land_Auction_SWP391.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    List<News> findTop3ByOrderByCreatedDateDesc();
    List<News> findByStaff_AccountId(int accountId);

    @Query("SELECT a FROM News a WHERE "
            + "(:tagIds IS NULL OR (SELECT COUNT(t) FROM a.tags t WHERE t.tagId IN :tagIds) = :#{#tagIds == null ? 0 : #tagIds.size()}) AND "
            + "(:keyword IS NULL OR a.title LIKE %:keyword%)")
    Page<News> filterNews(
            @Param("tagIds") List<Integer> tagIds,
            @Param("keyword") String keyword,
            Pageable pageable);


    Page<News> findAll(Pageable pageable);
}
