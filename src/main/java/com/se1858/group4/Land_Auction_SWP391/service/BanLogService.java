package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.BanLog;
import com.se1858.group4.Land_Auction_SWP391.repository.BanLogRepository;

import java.util.List;

public class BanLogService {
    BanLogRepository banLogRepository;
    public BanLogService(BanLogRepository banLogRepository) {
        this.banLogRepository = banLogRepository;
    }

    public List<BanLog> findAll() {
        return banLogRepository.findAll();
    }
}
