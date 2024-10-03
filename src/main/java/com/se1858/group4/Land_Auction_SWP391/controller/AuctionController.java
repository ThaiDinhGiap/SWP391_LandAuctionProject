package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.AuctionSession;
import com.se1858.group4.Land_Auction_SWP391.service.AuctionService;
import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@Controller
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Xử lý yêu cầu truy cập trang đấu giá
     * @param auctionId ID của phiên đấu giá
     * @param model đối tượng Model để truyền dữ liệu đến View
     * @return tên của trang đấu giá
     */
    @GetMapping("/auction/{auctionId}")
    public String accessAuction(@PathVariable int auctionId, Model model) {
        // Lấy thông tin người dùng hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Truy vấn Account từ cơ sở dữ liệu dựa trên username
        Account currentAccount = accountRepository.findByUsername(username);
        if (currentAccount == null) {
            model.addAttribute("message", "User account not found.");
            return "error";
        }

        int accountId = currentAccount.getAccountId();

        // Kiểm tra xem người dùng có được phép truy cập phiên đấu giá không
        if (!auctionService.isUserAllowedToAccessAuction(auctionId, accountId)) {
            model.addAttribute("message", "You are not allowed to access this auction.");
            return "error";
        }

        // Lấy thông tin phiên đấu giá
        AuctionSession auctionSession = auctionService.getAuctionSessionById(auctionId);

        // Truyền dữ liệu phiên đấu giá đến View
        model.addAttribute("auctionSession", auctionSession);
        model.addAttribute("accountCustomer", currentAccount);

        // Trả về tên của view đấu giá
        return "customer/auctionPage"; // Chắc chắn rằng bạn đã có file auctionPage.html trong thư mục templates
    }
}
