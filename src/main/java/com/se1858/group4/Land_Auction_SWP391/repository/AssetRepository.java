package com.se1858.group4.Land_Auction_SWP391.repository;


import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface AssetRepository extends JpaRepository<Asset, Integer> {
    @Query("SELECT a FROM Asset a WHERE "
            + "(:tagIds IS NULL OR (SELECT COUNT(t) FROM a.tags t WHERE t.tagId IN :tagIds) = :#{#tagIds == null ? 0 : #tagIds.size()}) AND "
            + "(:keyword IS NULL OR a.location LIKE %:keyword%) AND "
            + "(:fromDate IS NULL OR a.createdDate >= :fromDate) AND "
            + "(:toDate IS NULL OR a.createdDate <= :toDate)")
    List<Asset> filterAssets(
            @Param("tagIds") List<Integer> tagIds,
            @Param("keyword") String keyword,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);
}

