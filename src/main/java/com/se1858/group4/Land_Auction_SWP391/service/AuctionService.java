package com.se1858.group4.Land_Auction_SWP391.service;


import com.se1858.group4.Land_Auction_SWP391.dto.AuctionSessionDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.*;
import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.AssetRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.AuctionRegisterRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.AuctionSessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class AuctionService {
    private AuctionRegisterRepository auctionRegisterRepository;
    private AuctionSessionRepository auctionSessionRepository;
    private NotificationService notificationService;
    private AccountRepository accountRepository;
    private AssetRepository assetRepository;

    public AuctionService(AuctionRegisterRepository auctionRegisterRepository, AuctionSessionRepository auctionSessionRepository,
                          NotificationService notificationService, AccountRepository accountRepository, AssetRepository assetRepository) {
        this.auctionRegisterRepository = auctionRegisterRepository;
        this.auctionSessionRepository = auctionSessionRepository;
        this.notificationService = notificationService;
        this.accountRepository = accountRepository;
        this.assetRepository = assetRepository;
    }

    // Kiểm tra xem người dùng có được phép truy cập vào phiên đấu giá hay không
    public boolean isUserAllowedToAccessAuction(int auctionId, int accountId) {
        Optional<AuctionRegister> register = auctionRegisterRepository.findByAuction_AuctionIdAndBuyer_AccountIdAndRegisterStatus(auctionId, accountId, "Confirmed");
        return register.isPresent();
    }

    public AuctionSession getAuctionSessionById(int auctionId) {
        Optional<AuctionSession> auctionSessionOptional = auctionSessionRepository.findById(auctionId);
        if (auctionSessionOptional.isPresent()) {
            AuctionSession auctionSession = auctionSessionOptional.get();
//            return auctionSession;
            if(LocalDateTime.now().isAfter(auctionSession.getStartTime())&&auctionSession.getStatus().equals("Upcoming")) {
                AuctionSessionDTO auctionSessionDTO = new AuctionSessionDTO();
                auctionSessionDTO.setAuctionSession(auctionSession);
                auctionSessionDTO.getAuctionSession().setStatus("Ongoing");
                return updateAuctionSession(auctionSessionDTO);
            }
            else return auctionSession;
        } else {
            return null;
        }
    }

    public List<AuctionSession> getAllAutcion(){
        List<AuctionSession> list = auctionSessionRepository.findAll();
        for(AuctionSession auctionSession : list) {
            if(LocalDateTime.now().isAfter(auctionSession.getStartTime())&&auctionSession.getStatus().equals("Upcoming")) {
                AuctionSessionDTO auctionSessionDTO = new AuctionSessionDTO();
                auctionSessionDTO.setAuctionSession(auctionSession);
                auctionSessionDTO.getAuctionSession().setStatus("Ongoing");
                updateAuctionSession(auctionSessionDTO);
            }
        }
        return list;
    }

    public Page<AuctionSession> filterAuctionSessions(String keyword, LocalDate fromDate, LocalDate toDate, String status, int page) {
        LocalDateTime fromDateTime = (fromDate != null) ? fromDate.atStartOfDay() : null;
        LocalDateTime toDateTime = (toDate != null) ? toDate.atTime(23, 59, 59) : null;
        Pageable pageable = PageRequest.of(page, 4);
        return auctionSessionRepository.filterAuctionSessions(keyword, fromDateTime, toDateTime, status, pageable);
    }

    public Page<AuctionSession> getAuctions(int page) {
        Pageable pageable = PageRequest.of(page, 4);
//        return auctionSessionRepository.findAll(pageable);
        Page<AuctionSession> list = auctionSessionRepository.findAll(pageable);
        for(AuctionSession auctionSession : list.stream().toList()) {
            if(LocalDateTime.now().isAfter(auctionSession.getStartTime())&&auctionSession.getStatus().equals("Upcoming")) {
                AuctionSessionDTO auctionSessionDTO = new AuctionSessionDTO();
                auctionSessionDTO.setAuctionSession(auctionSession);
                auctionSessionDTO.getAuctionSession().setStatus("Ongoing");
                updateAuctionSession(auctionSessionDTO);
            }
        }
        return list;
    }

    public AuctionSession createAuctionSession(AuctionSession auctionSession) {
        return auctionSessionRepository.save(auctionSession);
    }


    public Page<AuctionSession> getAllAuctionSessionsByAuctioneerId(int auctioneerId, Pageable pageable) {
//        return auctionSessionRepository.findByAuctioneerId(auctioneerId, pageable);
        Page<AuctionSession> list = auctionSessionRepository.findByAuctioneerId(auctioneerId, pageable);
        for(AuctionSession auctionSession : list.stream().toList()) {
            if(LocalDateTime.now().isAfter(auctionSession.getStartTime())&&auctionSession.getStatus().equals("Upcoming")) {
                AuctionSessionDTO auctionSessionDTO = new AuctionSessionDTO();
                auctionSessionDTO.setAuctionSession(auctionSession);
                auctionSessionDTO.getAuctionSession().setStatus("Ongoing");
                updateAuctionSession(auctionSessionDTO);
            }
        }
        return list;
    }

    public Page<AuctionSession> searchAuctionSessionsByAuctioneerIdAndName(int auctioneerId, String auctionName, Pageable pageable) {
//        return auctionSessionRepository.findByAuctioneerIdAndAuctionNameContainingIgnoreCase(auctioneerId, auctionName, pageable);
        Page<AuctionSession> list = auctionSessionRepository.findByAuctioneerIdAndAuctionNameContainingIgnoreCase(auctioneerId, auctionName, pageable);
        for(AuctionSession auctionSession : list.stream().toList()) {
            if(LocalDateTime.now().isAfter(auctionSession.getStartTime())&&auctionSession.getStatus().equals("Upcoming")) {
                AuctionSessionDTO auctionSessionDTO = new AuctionSessionDTO();
                auctionSessionDTO.setAuctionSession(auctionSession);
                auctionSessionDTO.getAuctionSession().setStatus("Ongoing");
                updateAuctionSession(auctionSessionDTO);
            }
        }
        return list;
    }



    public void cancelAuction(int auctionId){
        AuctionSession auctionSession = getAuctionSessionById(auctionId);
        if(auctionSession != null){
            auctionSession.setStatus("Ending");
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

    public void finalizeAuction(int auctionSessionId, Long dealPrice) {
        AuctionSession auctionSession = auctionSessionRepository.findById(auctionSessionId)
                .orElseThrow(() -> new RuntimeException("Auction session not found"));
        auctionSession.setActualEndTime(LocalDateTime.now());
        auctionSession.setStatus("Ending");
        auctionSession.setDealedPrice(dealPrice);
        auctionSessionRepository.save(auctionSession);
        sendAuctionEndNotifications(auctionSession);
    }

    public void sendAuctionEndNotifications(AuctionSession auctionSession) {
        List<AuctionRegister> registers = auctionRegisterRepository.findByAuctionIdWithNotifications(auctionSession.getAuctionId());
        for (AuctionRegister register : registers) {
            Notification notification = new Notification();
            notification.setContent("The auction has ended.");
            notification.setCreatedDate(LocalDateTime.now());
            notification.setReadStatus("unread");
            notification.setAuction(register.getAuction());
            if ("Winner".equals(register.getResult())) {
                notification.setContent("Congratulations! You are the winner of the auction! We will send contract for you as soon as by email. Please check carefully!");
            }
            Account buyer = accountRepository.findById(register.getBuyer().getAccountId())
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            notification.addAccount(buyer);
            notificationService.saveNotification(notification);
            buyer.addNotification(notification);
            accountRepository.save(buyer);
            notificationService.sendNotification(notification);
        }
    }

    @Scheduled(fixedRate = 6000)
    public void checkAuctionEnd() {
        List<AuctionSession> activeAuctions = auctionSessionRepository.findAll();
        for (AuctionSession auction : activeAuctions) {
            if (auction.getActualEndTime() != null && auction.getStatus().equals("Ongoing") &&LocalDateTime.now().isAfter(auction.getActualEndTime())) {
                auction.setStatus("Ending");
                auctionSessionRepository.save(auction);
                sendAuctionEndNotifications(auction);
            }
        }
    }

}



