package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email) {
        accountService.registerUser(username, password, email);
        return "redirect:/verify";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String otp) {
        if (accountService.verifyOtp(otp)) {
            return "redirect:/showMyLoginPage"; // OTP verified, go to login
        } else {
            return "redirect:/verify?error"; // Verification failed, retry
        }
    }
}
