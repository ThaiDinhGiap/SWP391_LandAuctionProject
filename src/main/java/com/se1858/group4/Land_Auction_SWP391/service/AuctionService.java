package com.se1858.group4.Land_Auction_SWP391.service;


import com.se1858.group4.Land_Auction_SWP391.dto.AuctionSessionDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.AuctionRegister;
import com.se1858.group4.Land_Auction_SWP391.entity.AuctionSession;
import com.se1858.group4.Land_Auction_SWP391.entity.Notification;
import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.AuctionRegisterRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.AuctionSessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    public AuctionService(AuctionRegisterRepository auctionRegisterRepository, AuctionSessionRepository auctionSessionRepository, NotificationService notificationService, AccountRepository accountRepository) {
        this.auctionRegisterRepository = auctionRegisterRepository;
        this.auctionSessionRepository = auctionSessionRepository;
        this.notificationService = notificationService;
        this.accountRepository = accountRepository;
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

    public List<AuctionSession> filterAuctionSessions(String keyword, LocalDate fromDate, LocalDate toDate, String status) {
        LocalDateTime fromDateTime = (fromDate != null) ? fromDate.atStartOfDay() : null;
        LocalDateTime toDateTime = (toDate != null) ? toDate.atTime(23, 59, 59) : null;


        return auctionSessionRepository.filterAuctionSessions(keyword, fromDateTime, toDateTime, status);
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
        // Tìm phiên đấu giá
        AuctionSession auctionSession = auctionSessionRepository.findById(auctionSessionId)
                .orElseThrow(() -> new RuntimeException("Auction session not found"));

        // Cập nhật thời gian kết thúc thực tế
        auctionSession.setActualEndTime(LocalDateTime.now());

        // Cập nhật trạng thái đăng ký đấu giá
        auctionSession.setStatus("Completed");

        // Cap nhat deal price
        auctionSession.setDealedPrice(dealPrice);

        // Lưu lại thông tin cập nhật
        auctionSessionRepository.save(auctionSession);

        // Gửi thông báo kết thúc đấu giá
        sendAuctionEndNotifications(auctionSession);
    }

    public void sendAuctionEndNotifications(AuctionSession auctionSession) {
        List<AuctionRegister> registers = auctionRegisterRepository.findByAuctionIdWithNotifications(auctionSession.getAuctionId());

        for (AuctionRegister register : registers) {
            Notification notification = new Notification();
            notification.setContent("The auction " + auctionSession.getAuctionName() + " has ended.");
            notification.setCreatedDate(LocalDateTime.now());
            notification.setReadStatus("unread");

            if ("Winner".equals(register.getResult())) {
                notification.setContent("Congratulations! You are the winner of the auction " + auctionSession.getAuctionName() + ". We will send contract for you as soon as by email. Please check carefully!");
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

    @Scheduled(fixedRate = 6000) // Kiểm tra mỗi phút
    public void checkAuctionEnd() {
        List<AuctionSession> activeAuctions = auctionSessionRepository.findByStatus("OnGoing");
        long now = System.currentTimeMillis();
        for (AuctionSession auction : activeAuctions) {
            if (auction.getExpectedEndTime().getSecond() >= now) {
                sendAuctionEndNotifications(auction);
            }
        }
    }

}



