package com.se1858.group4.Land_Auction_SWP391.repository;

import com.se1858.group4.Land_Auction_SWP391.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Page<Task> findByAuctioneer_AccountIdAndStatus(Integer auctioneerId, String status, Pageable pageable);

    Page<Task> findByAuctioneer_AccountIdAndStatusAndAsset_LocationContainingOrPropertyAgent_Staff_FullNameContaining(
            Integer auctioneerId, String status, String location, String propertyAgentName, Pageable pageable);
}


