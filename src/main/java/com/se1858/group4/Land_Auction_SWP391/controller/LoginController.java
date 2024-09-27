package com.se1858.group4.Land_Auction_SWP391.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "access-denied";
    }

    @GetMapping("/admin/test")
    public String showAdminPage() {
        return "test";
    }

    @GetMapping("/customer/homepage_customer")
    public String showCustomerPage() {
        return "homepage_customer";
    }
}
