package com.se1858.group4.Land_Auction_SWP391.service;

import ch.qos.logback.core.model.Model;
import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.BanLog;
import com.se1858.group4.Land_Auction_SWP391.entity.Staff;
import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.BanLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private BanLogRepository banLogRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account findAccountById(int id) {
        return accountRepository.findById(id).get();
    }

    public List<Staff> findAllStaffsByRole(String role) {
        List<Account> listAccount = accountRepository.findAll();
        List<Staff> result = null;
        for (Account account : listAccount) {
            if (account.getRole().getRoleName().equals(role)) {
                if (result == null) {
                    result = new ArrayList<Staff>();
                }
                result.add(account.getStaff());
            }
        }
        return result;
    }

    public List<Account> findAllAccount() {
        return accountRepository.findAll();
    }

    public Account save(Account account) {
        // Set current time
        account.setRegistrationDate(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public void update(Account adminId, Account accountId, String reason) {
        Account existingAccount = accountRepository.findById(accountId.getAccountId()).
                orElseThrow(() -> new IllegalArgumentException("Account not found"));

        List<String> changedFields = new ArrayList<>();
        if (existingAccount.getStatus() == (accountId.getStatus())) {
            changedFields.add("status");
        }
        // Cập nhật account
        accountRepository.save(accountId);

        // Ghi log nếu có thay đổi
        if (!changedFields.isEmpty()) {
            BanLog log = new BanLog();
            log.setAdmin(adminId);
            log.setAccount(accountId);
            log.setTimestamp(LocalDateTime.now());
            log.setReason(reason);

            banLogRepository.save(log);
        }
    }

    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

}