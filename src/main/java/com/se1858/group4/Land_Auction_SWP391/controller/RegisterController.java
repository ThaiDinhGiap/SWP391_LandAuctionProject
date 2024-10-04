package com.se1858.group4.Land_Auction_SWP391.controller;


import com.se1858.group4.Land_Auction_SWP391.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";  // Return the registration form (register.html)
    }

    @GetMapping("/verify-otp")
    public String showVerifyPage() {
        return "verify";
    }



    @GetMapping("/otp-success")
 public String showOtpSuccessPage() {
        return "otp-success";
    }


    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email) {
        accountService.registerUser(username, password, email);
        return "redirect:/verify-otp";  // After registration, redirect to OTP verification
    }


    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String otp, Model model) {
        if (accountService.verifyOtp(otp)) {
            model.addAttribute("message", "OTP is correct. You can now log in.");
            return "redirect:/otp-success";
        } else {
            model.addAttribute("error", "Invalid OTP. Please try again.");
            return "redirect:/verify-otp";
        }
    }


    @PostMapping("/resend-otp")
    public String resendOtp() {
        accountService.resendOtp();
        return "redirect:/verify-otp";  // Redirect back to OTP page
    }


}