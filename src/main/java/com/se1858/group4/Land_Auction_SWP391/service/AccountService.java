package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.Staff;
import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public Account findAccountById(int id) {
        return accountRepository.findById(id).get();
    }
    public List<Staff> findAllStaffsByRole(String role) {
        List<Account> listAccount = accountRepository.findAll();
        List<Staff> result = null;
        for(Account account : listAccount) {
            if(account.getRole().getRoleName().equals(role)) {
                if(result == null) {
                    result = new ArrayList<Staff>();
                }
                result.add(account.getStaff());
            }
        }
        return result;
    }
}
