package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.BidRequestDTO;
import com.se1858.group4.Land_Auction_SWP391.dto.BidResponseDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.AuctionSession;
import com.se1858.group4.Land_Auction_SWP391.entity.Bid;
import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import com.se1858.group4.Land_Auction_SWP391.service.AuctionRegisterService;
import com.se1858.group4.Land_Auction_SWP391.service.AuctionService;
import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
import com.se1858.group4.Land_Auction_SWP391.service.BidService;
import com.se1858.group4.Land_Auction_SWP391.websocket.EndAuctionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class AuctionController {

    private AuctionService auctionService;
    private AuctionRegisterService auctionRegisterService;
    private BidService bidService;
    private UserDetailsService userDetailsService;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public AuctionController(AuctionService auctionService, AuctionRegisterService auctionRegisterService, BidService bidService, UserDetailsService userDetailsService, SimpMessagingTemplate simpMessagingTemplate) {
        this.auctionService = auctionService;
        this.auctionRegisterService = auctionRegisterService;
        this.bidService = bidService;
        this.userDetailsService = userDetailsService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    /**
     * Xử lý yêu cầu truy cập trang đấu giá
     * @param auctionId ID của phiên đấu giá
     * @param model đối tượng Model để truyền dữ liệu đến View
     * @return tên của trang đấu giá
     */
    @GetMapping("/auction/{auctionId}")
    public String accessAuction(@PathVariable int auctionId, Model model) {
        // Truy vấn Account now
        Account currentAccount = userDetailsService.accountAuthenticated();
        if (currentAccount == null) {
            model.addAttribute("message", "User account not found.");
            return "error";
        }

        // Kiểm tra xem người dùng có được phép truy cập phiên đấu giá không
        if (!auctionService.isUserAllowedToAccessAuction(auctionId, currentAccount.getAccountId())) {
            model.addAttribute("message", "You are not allowed to access this auction.");
            return "error";
        }

        // Lấy thông tin phiên đấu giá
        AuctionSession auctionSession = auctionService.getAuctionSessionById(auctionId);
        List<Bid> bidList = bidService.getAllBidsByAuctionId(auctionId);

        // Truyền dữ liệu phiên đấu giá đến View
        model.addAttribute("auctionSession", auctionSession);
        model.addAttribute("accountCustomer", currentAccount);
        model.addAttribute("bidList", bidList);

        // Trả về tên của view đấu giá
        return "customer/auctionPage"; // Chắc chắn rằng bạn đã có file auctionPage.html trong thư mục templates
    }

    @MessageMapping("/bid")
    public void handleBid(@Payload BidRequestDTO bidRequestDTO) {
        // Xử lý bid
        BidResponseDTO response = bidService.placeBid(bidRequestDTO);
        // Gửi tin nhắn đến topic với auctionId cụ thể
        simpMessagingTemplate.convertAndSend("/topic/auction/" + bidRequestDTO.getAuctionId(), response);
    }

    @MessageMapping("/endAuction")
    public void handleEndAuctionMessage(@Payload EndAuctionMessage endAuctionMessage) {
        // Kiểm tra kiểu tin nhắn kết thúc đấu giá
        if ("Completed".equals(endAuctionMessage.getTypeMessage())) {
            // Xử lý logic khi đấu giá kết thúc thành công
            auctionRegisterService.finalizeAuctionSession(endAuctionMessage.getAuctionId());
            auctionService.finalizeAuction(endAuctionMessage.getAuctionId(), endAuctionMessage.getDealPrice());
            // Thông báo tới các người dùng rằng đấu giá đã kết thúc
        } else if ("Cancelled".equals(endAuctionMessage.getTypeMessage())) {
            // Logic nếu đấu giá bị hủy
            // Thông báo hủy đấu giá
        }
    }

}
