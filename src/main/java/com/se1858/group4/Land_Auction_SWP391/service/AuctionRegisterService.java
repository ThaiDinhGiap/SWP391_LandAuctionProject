package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.AuctionRegister;
import com.se1858.group4.Land_Auction_SWP391.repository.AuctionRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuctionRegisterService {
    private AuctionRegisterRepository registerRepository;
    @Autowired
    public AuctionRegisterService(AuctionRegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }
    public AuctionRegister getAuctionRegister(int auctionId, int accountId) {
        Optional<AuctionRegister> auctionRegister = registerRepository.findByAuction_AuctionIdAndBuyer_AccountId(auctionId,accountId);
        if(auctionRegister.isPresent()){
            return auctionRegister.get();
        }
        else return null;
    }
    public AuctionRegister getAuctionRegisterById(int auctionRegisterId) {
        Optional<AuctionRegister> auctionRegister = registerRepository.findById(auctionRegisterId);
        if(auctionRegister.isPresent()){
            return auctionRegister.get();
        }
        else return null;
    }
    public AuctionRegister createAuctionRegister(AuctionRegister auctionRegister) {
        return registerRepository.save(auctionRegister);
    }
    public AuctionRegister updateRegisterStatus(AuctionRegister auctionRegister) {
        AuctionRegister existingAuctionRegister = getAuctionRegister(auctionRegister.getAuction().getAuctionId(),auctionRegister.getBuyer().getAccountId());
        if(existingAuctionRegister!=null) {
            existingAuctionRegister.setRegisterStatus(auctionRegister.getRegisterStatus());
            return registerRepository.save(existingAuctionRegister);
        }
        else return null;
    }
}
