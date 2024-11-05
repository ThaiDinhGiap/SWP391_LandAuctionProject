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

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               Model model) {
        // Check if the username already exists
        if (accountService.checkUsernameExists(username)) {
            model.addAttribute("errorUsernameMessage", "Username already exists. Please choose another one.");
            return "register";
        }

        // Check if the email already exists
        if (accountService.checkEmailExists(email)) {
            model.addAttribute("errorEmailMessage", "Email already exists. Please use another one.");
            return "register";
        }
        if (username.length() < 6) {
            model.addAttribute("errorUsernameMessage", "Username must be at least 6 character.");
            return "register";
        }
        if (password.length() < 6) {
            model.addAttribute("errorPasswordMessage", "Password must be at least 6 character.");
            return "register";
        }
        // Register the user if no issues
        accountService.registerUser(username, password, email, model);
        return "redirect:/verify-otp";  // After registration, redirect to OTP verification
    }


    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String otp, Model model) {
        if (accountService.verifyOtp(otp)) {
            model.addAttribute("message", "OTP is correct! You can now log in.");
            return "verify";
        } else {
            model.addAttribute("error", "Invalid OTP. Please try again.");
            return "verify";
        }
    }

    @PostMapping("/resend-otp")
    public String resendOtp() {
        accountService.resendOtp();
        return "redirect:/verify-otp";  // Redirect back to OTP page
    }
}