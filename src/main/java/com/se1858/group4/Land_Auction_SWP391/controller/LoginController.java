package com.se1858.group4.Land_Auction_SWP391.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/default")
    public String defaultAfterLogin() {
        if (hasRole("ROLE_Customer")) {
            return "redirect:/customer/home";
        }
        if (hasRole("ROLE_Admin")) {
            return "redirect:/admin/home";
        }
        return "redirect:/";
    }

    private boolean hasRole(String role) {
        return org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }

    @GetMapping({"/showMyLoginPage"})
    public String showLoginPage() {
        return "showMyLoginPage";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "access-denied";
    }

    @GetMapping("/customer/home")
    public String customerHome() {
        return "homepage_customer";
    }

    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin";
    }
}
