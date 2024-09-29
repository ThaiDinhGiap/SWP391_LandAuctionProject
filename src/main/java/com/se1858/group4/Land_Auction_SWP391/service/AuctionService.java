package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.AuctionRegister;
import com.se1858.group4.Land_Auction_SWP391.repository.AuctionRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuctionService {

    @Autowired
    private AuctionRegisterRepository auctionRegisterRepository;

    // Kiểm tra xem người dùng có được phép truy cập vào phiên đấu giá hay không
    public boolean isUserAllowedToAccessAuction(int auctionId, int accountId) {
        Optional<AuctionRegister> register = auctionRegisterRepository.findByAuction_AuctionIdAndBuyer_AccountIdAndRegisterStatus(auctionId, accountId, "accepted");
        return register.isPresent();
    }
}
