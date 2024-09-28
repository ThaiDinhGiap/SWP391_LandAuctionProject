package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public Account findAccountById(int id) {
        return accountRepository.findById(id).get();
    }
}
