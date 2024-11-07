package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@SessionAttributes("verificationCode") // Sử dụng @SessionAttributes để lưu mã OTP vào session
public class ContactController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-contact-form")
    public String handleContactForm(@RequestParam String name, @RequestParam String email, @RequestParam String phone,
                                    @RequestParam String job, @RequestParam String message, Model model) {
        String verificationCode = generateVerificationCode();

        emailService.sendVerificationEmail(email, verificationCode);

        model.addAttribute("email", email);
        model.addAttribute("verificationCode", verificationCode); // Lưu mã OTP vào session
        return "customer/verify-email";
    }

    @GetMapping("/verify-email")
    public String showVerifyPage(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "customer/verify-email";
    }

    @PostMapping("/verify-email")
    public String verifyEmail(@RequestParam String verificationCode, @RequestParam String email,
                              @ModelAttribute("verificationCode") String correctCode, Model model) {
        if (verificationCode.equals(correctCode)) {
            emailService.sendContactConfirmationEmail(email);

            model.addAttribute("verificationSuccess", true);
            model.addAttribute("email", email);
            return "customer/verify-email";
        } else {
            model.addAttribute("error", true);
            model.addAttribute("email", email);
            return "customer/verify-email";
        }
    }

    private String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}

