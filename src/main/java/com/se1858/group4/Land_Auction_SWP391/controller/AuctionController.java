package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.AuctionSession;
import com.se1858.group4.Land_Auction_SWP391.entity.Bid;
import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import com.se1858.group4.Land_Auction_SWP391.service.AuctionService;
import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
import com.se1858.group4.Land_Auction_SWP391.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class AuctionController {

    private AuctionService auctionService;
    private AccountRepository accountRepository;
    private BidService bidService;
    private UserDetailsService userDetailsService;

    @Autowired
    public AuctionController(AuctionService auctionService, AccountRepository accountRepository, BidService bidService, UserDetailsService userDetailsService) {
        this.auctionService = auctionService;
        this.accountRepository = accountRepository;
        this.bidService = bidService;
        this.userDetailsService = userDetailsService;
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
}
