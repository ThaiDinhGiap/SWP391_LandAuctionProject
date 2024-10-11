package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatBotPageController {

    private final UserDetailsService userDetailsService;

    @Autowired
    public ChatBotPageController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/chatbot")
    public String chatbotPage() {
        return "chatbot"; // Trả về tên file HTML chatbot.html
    }

    @GetMapping("/chatbothome")
    public String chatbotHomePage() {
        return "customer/homepage"; // Trả về tên file HTML chatbot.html
    }

    @GetMapping("/care")
    public String careHomePage() {
        return "customerCare/dashboard"; // Trả về tên file HTML chatbot.html
    }
}

