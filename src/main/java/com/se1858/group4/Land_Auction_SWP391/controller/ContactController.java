package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@SessionAttributes({"name", "email", "phone", "job", "message", "verificationCode"})
public class ContactController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-contact-form")
    public String handleContactForm(@RequestParam String name, @RequestParam String email, @RequestParam String phone,
                                    @RequestParam String job, @RequestParam String message, Model model) {
        // Lưu thông tin vào session
        model.addAttribute("name", name);
        model.addAttribute("email", email);
        model.addAttribute("phone", phone);
        model.addAttribute("job", job);
        model.addAttribute("message", message);

        
        String verificationCode = generateVerificationCode();
        emailService.sendVerificationEmail(email, verificationCode);


        model.addAttribute("verificationCode", verificationCode);
        return "customer/verify-email";
    }

    @PostMapping("/verify-email")
    public String verifyEmail(@RequestParam String verificationCode, @ModelAttribute("verificationCode") String correctCode,
                              @ModelAttribute("name") String name, @ModelAttribute("email") String email,
                              @ModelAttribute("phone") String phone, @ModelAttribute("job") String job,
                              @ModelAttribute("message") String message, Model model) {

        if (verificationCode.equals(correctCode)) {

            emailService.sendContactConfirmationEmail(email);
            emailService.sendContactFormDetails("vietlandaution@gmail.com", name, email, phone, job, message);

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



