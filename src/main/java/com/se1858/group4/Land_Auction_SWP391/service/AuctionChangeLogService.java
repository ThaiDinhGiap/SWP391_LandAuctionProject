package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.AuctionChangeLog;
import com.se1858.group4.Land_Auction_SWP391.repository.AuctionChangeLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionChangeLogService {
    private AuctionChangeLogRepository auctionChangeLogRepository;
    private AuctionService auctionService;

    public AuctionChangeLogService(AuctionChangeLogRepository auctionChangeLogRepository, AuctionService auctionService) {
        this.auctionChangeLogRepository = auctionChangeLogRepository;
        this.auctionService = auctionService;
    }

    public AuctionChangeLog createAuctionChange(String changeType, String reason, int auctionId) {
        AuctionChangeLog auctionChangeLog = new AuctionChangeLog();
        auctionChangeLog.setChangeType(changeType);
        auctionChangeLog.setReason(reason);
        auctionChangeLog.setAuction_session(auctionService.getAuctionSessionById(auctionId));
        auctionChangeLog.setTime(LocalDateTime.now());
        return auctionChangeLogRepository.save(auctionChangeLog);
    }

    public List<AuctionChangeLog> getAllAuctionChangeLog(int auctionId) {
        List<AuctionChangeLog> list = auctionChangeLogRepository.findByAuctionId(auctionId);
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }
}
