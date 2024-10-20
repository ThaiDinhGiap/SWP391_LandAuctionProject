package com.se1858.group4.Land_Auction_SWP391.service;


import com.se1858.group4.Land_Auction_SWP391.dto.AuctionSessionDTO;
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
    private AuctionRegisterRepository auctionRegisterRepository;
    private AuctionSessionRepository auctionSessionRepository;

    public AuctionService(AuctionRegisterRepository auctionRegisterRepository, AuctionSessionRepository auctionSessionRepository) {
        this.auctionRegisterRepository = auctionRegisterRepository;
        this.auctionSessionRepository = auctionSessionRepository;
    }

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
            return null;
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

    public AuctionSession createAuctionSession(AuctionSession auctionSession) {
        return auctionSessionRepository.save(auctionSession);
    }

    public List<AuctionSession> getAllAuctionSessionsByAuctioneerId(int auctioneerId) {
        List<AuctionSession> list=auctionSessionRepository.findByAuctioneerId(auctioneerId);
        if(list.isEmpty()){
            return null;
        }
        else return list;
    }

    public void cancelAuction(int auctionId){
        AuctionSession auctionSession = getAuctionSessionById(auctionId);
        if(auctionSession != null){
            auctionSession.setStatus("Cancelled");
        }
    }

    public AuctionSession updateAuctionSession(AuctionSessionDTO auctionSessionDTO) {
        AuctionSession newAuctionSession=auctionSessionDTO.getAuctionSession();
        AuctionSession existingAuctionSession=getAuctionSessionById(newAuctionSession.getAuctionId());
        existingAuctionSession.setAuctionName(newAuctionSession.getAuctionName());
        existingAuctionSession.setStartTime(newAuctionSession.getStartTime());
        existingAuctionSession.setExpectedEndTime(newAuctionSession.getExpectedEndTime());
        existingAuctionSession.setStartingPrice(newAuctionSession.getStartingPrice());
        existingAuctionSession.setMinimumBidIncrement(newAuctionSession.getMinimumBidIncrement());
        existingAuctionSession.setRegistrationOpenDate(newAuctionSession.getRegistrationOpenDate());
        existingAuctionSession.setRegistrationCloseDate(newAuctionSession.getRegistrationCloseDate());
        existingAuctionSession.setExtraTimeUnit(newAuctionSession.getExtraTimeUnit());
        existingAuctionSession.setDeposit(newAuctionSession.getDeposit());
        existingAuctionSession.setRegisterFee(newAuctionSession.getRegisterFee());
        return auctionSessionRepository.save(existingAuctionSession);
    }
}



