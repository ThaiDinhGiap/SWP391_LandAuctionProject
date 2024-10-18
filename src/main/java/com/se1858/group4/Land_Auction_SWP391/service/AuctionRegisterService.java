package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.AuctionRegister;
import com.se1858.group4.Land_Auction_SWP391.repository.AuctionRegisterRepository;
import org.springframework.stereotype.Service;

@Service
public class AuctionRegisterService {
    private AuctionRegisterRepository registerRepository;
    public AuctionRegisterService(AuctionRegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }
    public AuctionRegister getAuctionRegister(int auctionId) {
        AuctionRegister auctionRegister = registerRepository.findById(auctionId).get();
        if (auctionRegister == null) {
            return null;
        }
        else{
            return auctionRegister;
        }
    }
    public AuctionRegister createAuctionRegister(AuctionRegister auctionRegister) {
        return registerRepository.save(auctionRegister);
    }
}
