package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.BanLog;
import com.se1858.group4.Land_Auction_SWP391.repository.BanLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BanLogService {
    BanLogRepository banLogRepository;

    public BanLogService(BanLogRepository banLogRepository) {
        this.banLogRepository = banLogRepository;
    }

    public List<BanLog> findAllBanLog() {
        return banLogRepository.findAll();
    }

    public void logBan(Account adminId, Account accountId, String reason) {
        BanLog banLog = new BanLog();
        banLog.setAccount(accountId);
        banLog.setAdmin(adminId);
        banLog.setTimestamp(LocalDateTime.now());
        banLog.setReason(reason);

        banLogRepository.save(banLog);
    }

    public void save(BanLog banLog) {
        banLogRepository.save(banLog);
    }
}
