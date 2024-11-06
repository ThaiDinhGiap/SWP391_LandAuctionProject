package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class ContactController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-contact-form")
    public String handleContactForm(@RequestParam String name, @RequestParam String email, @RequestParam String phone,
                                    @RequestParam String job, @RequestParam String message, Model model) {
        String verificationCode = generateVerificationCode();

        emailService.sendVerificationEmail(email, verificationCode);


        model.addAttribute("email", email);
        model.addAttribute("verificationCode", verificationCode); // Thêm mã xác minh để kiểm tra
        return "customer/verify-email";
    }

    @GetMapping("/verify-email")
    public String showVerifyPage(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "customer/verify-email";
    }

    @PostMapping("/verify-email")
    public String verifyEmail(@RequestParam String verificationCode, @RequestParam String email, Model model) {

        if (isValidVerificationCode(verificationCode)) {

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

    private boolean isValidVerificationCode(String code) {

        return true;
    }
}
