package com.se1858.group4.Land_Auction_SWP391.security;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService {
    private AccountRepository accountRepository;

    @Autowired
    public UserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account accountAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return accountRepository.findByUsername(((User) principal).getUsername());
            }
        }
        return null;
    }
}
