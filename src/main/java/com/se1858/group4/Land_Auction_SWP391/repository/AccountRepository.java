package com.se1858.group4.Land_Auction_SWP391.repository;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByUsername(String username);
    boolean existsByEmail(String email);  // Method to check if the account exists
    Account findByEmail(String email);
    @Query("SELECT u FROM Account  u WHERE u.verificationCode = ?1")
    public Account findByVerificationCode(String code);
}
