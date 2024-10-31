package com.se1858.group4.Land_Auction_SWP391.repository;

import com.se1858.group4.Land_Auction_SWP391.entity.LocalAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface LocalAuthorityRepository extends JpaRepository<LocalAuthority, Integer> {
    @Query("SELECT l FROM LocalAuthority l WHERE l.localAuthorityName LIKE %:keyword% OR l.contactPerson LIKE %:keyword%")
    Page<LocalAuthority> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}

