package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.StaffDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import com.se1858.group4.Land_Auction_SWP391.utility.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/customercare")
public class CustomerCareController {
    private FileUploadUtil uploadFile;
    private UserDetailsService userDetailsService;

    @Autowired
    public CustomerCareController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/direct-chat")
    public String directChat() {
        return "customerCare/direct-chat";
    }

    @GetMapping("/history-chat")
    public String historyChat() {
        return "customerCare/history-chat";
    }

    @GetMapping("/chatbot-settings")
    public String chatbotSettings() {
        return "customerCare/chatbot-settings";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Account account = userDetailsService.accountAuthenticated();
        if (account != null) {
            StaffDTO staffDTO = new StaffDTO();
            staffDTO.setAccount(account);
            staffDTO.setStaff(account.getStaff());
            model.addAttribute("staffDTO", staffDTO);
        }
        return "customerCare/profile";
    }

    @PostMapping("/uploadAvatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile avatar, Model model) {
        Account account = userDetailsService.accountAuthenticated();
        if (account != null && avatar != null) {
            uploadFile.UploadAvatar(avatar, account);
        }
        return "redirect:/customercare/profile";
    }
}
