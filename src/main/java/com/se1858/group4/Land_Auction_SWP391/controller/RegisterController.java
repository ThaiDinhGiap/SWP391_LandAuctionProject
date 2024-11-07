package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private final AccountService accountService;

    @Autowired
    public RegisterController(AccountService accountService) {
        this.accountService = accountService;
    }


// Trong phương thức đăng ký, sử dụng RedirectAttributes thay vì Model để truyền thông báo lỗi

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               RedirectAttributes redirectAttributes) {

        if (accountService.checkUsernameExists(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username already exists. Please choose another one.");
            return "redirect:/showMyLoginPage?isRegister=true";
        }

        if (username.length() < 6) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username must be at least 6 characters.");
            return "redirect:/showMyLoginPage?isRegister=true";
        }

        if (accountService.checkEmailExists(email)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email already exists. Please use another one.");
            return "redirect:/showMyLoginPage?isRegister=true";
        }

        if (password.length() < 6) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 6 characters.");
            return "redirect:/showMyLoginPage?isRegister=true";
        }

        // Nếu không có lỗi nào, tiến hành đăng ký tài khoản
        accountService.registerUser(username, password, email, redirectAttributes);
        return "redirect:/verify-otp";
    }


    @GetMapping("/verify-otp")
    public String showVerifyPage() {
        return "verify";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String otp, Model model) {
        if (accountService.verifyOtp(otp)) {
            model.addAttribute("message", "OTP is correct! You can now log in.");
            return "redirect:/showMyLoginPage";
        } else {
            model.addAttribute("error", "Invalid OTP. Please try again.");
            return "verify";
        }
    }

    @PostMapping("/resend-otp")
    public String resendOtp() {
        accountService.resendOtp();
        return "redirect:/verify-otp";
    }
}
