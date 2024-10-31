package com.se1858.group4.Land_Auction_SWP391.repository;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByUsername(String username);

    Account findByEmail(String email);
//    Account findAccountByAccountId(int accountId);
//    Account findByEmail(String email);
//    List<Account> findByStatus(int i);
    boolean existsByUsername(String username);

    Page<Account> findByStatusAndVerify(Integer status, Integer verify, Pageable pageable);

    Page<Account> findByStatus(Integer status, Pageable pageable);

    Page<Account> findByVerify(Integer verify, Pageable pageable);

    Page<Account> findAll(Pageable pageable);

}



