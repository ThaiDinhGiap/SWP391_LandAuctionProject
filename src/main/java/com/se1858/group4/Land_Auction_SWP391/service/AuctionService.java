package com.se1858.group4.Land_Auction_SWP391.service;


import com.se1858.group4.Land_Auction_SWP391.entity.AuctionRegister;
import com.se1858.group4.Land_Auction_SWP391.entity.AuctionSession;
import com.se1858.group4.Land_Auction_SWP391.repository.AuctionRegisterRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.AuctionSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class AuctionService {


    @Autowired
    private AuctionRegisterRepository auctionRegisterRepository;


    @Autowired
    private AuctionSessionRepository auctionSessionRepository;


    // Kiểm tra xem người dùng có được phép truy cập vào phiên đấu giá hay không
    public boolean isUserAllowedToAccessAuction(int auctionId, int accountId) {
        Optional<AuctionRegister> register = auctionRegisterRepository.findByAuction_AuctionIdAndBuyer_AccountIdAndRegisterStatus(auctionId, accountId, "accepted");
        return register.isPresent();
    }


    public AuctionSession getAuctionSessionById(int auctionId) {
        Optional<AuctionSession> auctionSessionOptional = auctionSessionRepository.findById(auctionId);


        if (auctionSessionOptional.isPresent()) {
            return auctionSessionOptional.get();
        } else {
            throw new IllegalArgumentException("Auction session with ID " + auctionId + " not found.");
        }
    }


    public List<AuctionSession> getAllAutcion(){
        return auctionSessionRepository.findAll();
    }


    public List<AuctionSession> filterAuctionSessions(String keyword, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime fromDateTime = (fromDate != null) ? fromDate.atStartOfDay() : null;
        LocalDateTime toDateTime = (toDate != null) ? toDate.atTime(23, 59, 59) : null;


        return auctionSessionRepository.filterAuctionSessions(keyword, fromDateTime, toDateTime);
    }
}



