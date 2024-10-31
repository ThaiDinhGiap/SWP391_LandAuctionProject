package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.AuctionRegister;
import com.se1858.group4.Land_Auction_SWP391.entity.AuctionSession;
import com.se1858.group4.Land_Auction_SWP391.repository.AuctionRegisterRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.AuctionSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.se1858.group4.Land_Auction_SWP391.entity.Bid;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AuctionRegisterService {
    private AuctionRegisterRepository registerRepository;
    private AuctionSessionRepository sessionRepository;

    @Autowired
    public AuctionRegisterService(AuctionRegisterRepository registerRepository, AuctionSessionRepository sessionRepository) {
        this.registerRepository = registerRepository;
        this.sessionRepository = sessionRepository;
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
        AuctionRegister register = registerRepository.save(auctionRegister);
        register.setNickName("Anonymous"+register.getRegisterId());
        return registerRepository.save(register);
    }
    public AuctionRegister updateRegisterStatus(AuctionRegister auctionRegister) {
        AuctionRegister existingAuctionRegister = getAuctionRegister(auctionRegister.getAuction().getAuctionId(),auctionRegister.getBuyer().getAccountId());
        if(existingAuctionRegister!=null) {
            existingAuctionRegister.setRegisterStatus(auctionRegister.getRegisterStatus());
            existingAuctionRegister.setPurchaseStatus(auctionRegister.getPurchaseStatus());
            existingAuctionRegister.setDepositStatus(auctionRegister.getDepositStatus());
            return registerRepository.save(existingAuctionRegister);
        }
        else return null;
    }
    public List<AuctionRegister> getAllAuctionRegistersByAuctionId(int auctionId) {
        List<AuctionRegister> registerList = registerRepository.findByAuction_AuctionId(auctionId);
        if(registerList!=null){
            return registerList;
        }
        else return null;
    }
    public void finalizeAuctionSession(int auctionSessionId) {
        // Lấy danh sách đăng ký của phiên đấu giá
        List<AuctionRegister> registers = registerRepository.findByAuction_AuctionIdWithBids(auctionSessionId);
        AuctionSession auctionSession = sessionRepository.findById(auctionSessionId)
                .orElseThrow(() -> new RuntimeException("Auction session not found"));

        if (registers.isEmpty()) {
            System.out.println("No participants in this auction session."); // Logging or handling as needed
            return;
        }

        // Tìm bidAmount cao nhất của mỗi người đăng ký và thiết lập xếp hạng
        registers.forEach(register -> {
            Optional<Long> maxBidAmount = register.getBids().stream()
                    .map(Bid::getBidAmount)
                    .max(Long::compareTo);
            register.setMaxBidAmount(maxBidAmount.orElse(0L));
        });

        // Xếp hạng dựa trên maxBidAmount và gán trạng thái mua
        AtomicInteger currentRank = new AtomicInteger(1);

        registers.stream()
                .sorted(Comparator.comparing(AuctionRegister::getMaxBidAmount).reversed())
                .forEachOrdered(register -> {
                    int rank = currentRank.getAndIncrement();
                    register.setRank(rank);

                    // Cấp quyền mua cho người xếp hạng 1, những người khác không có quyền mua
                    if (rank == 1) {
                        register.setPurchaseStatus("Allowed");
                        register.setResult("Winner");
                        auctionSession.setWinner(register.getBuyer());
                    } else {
                        register.setPurchaseStatus("Not allowed");
                        register.setResult("Participant");
                    }

                    // Tất cả đặt cọc đều chuyển thành trạng thái "Locked"
                    register.setDepositStatus("Locked");
                });

        // Lưu tất cả thay đổi vào cơ sở dữ liệu
        registerRepository.saveAll(registers);
    }

    public void handleDepositForfeiture(int auctionSessionId, int rank) {
        // Lấy danh sách đăng ký của phiên đấu giá theo thứ tự rank tăng dần
        List<AuctionRegister> registers = registerRepository.findByAuction_AuctionIdOrderByRank(auctionSessionId);

        registers.stream()
                .filter(register -> register.getRank() == rank)
                .findFirst()
                .ifPresent(register -> {
                    // Cập nhật trạng thái cọc thành "Forfeited" cho người đang bỏ quyền mua
                    register.setDepositStatus("Forfeited");
                    register.setResult("Forfeited");
                    register.setPurchaseStatus("Not allowed");

                    // Tìm người tiếp theo để cấp quyền mua nếu họ không bỏ quyền đăng ký
                    registers.stream()
                            .filter(nextRegister -> nextRegister.getRank() > rank && nextRegister.getRegisterStatus().equals("Confirmed"))
                            .findFirst()
                            .ifPresent(nextRegister -> {
                                nextRegister.setPurchaseStatus("Allowed");
                                nextRegister.setResult("Winner");
                            });
                });

        // Lưu các thay đổi vào cơ sở dữ liệu
        registerRepository.saveAll(registers);
    }


}
