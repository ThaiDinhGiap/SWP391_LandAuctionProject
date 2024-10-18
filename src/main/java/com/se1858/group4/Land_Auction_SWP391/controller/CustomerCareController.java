package com.se1858.group4.Land_Auction_SWP391.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customercare")
public class CustomerCareController {
    @GetMapping("/dashboard")
    public String dashboard() {
        return "customerCare/dashboard";
    }
    @GetMapping("/direct-chat")
    public String directChat() {
        return "customerCare/direct-chat";
    }
    @GetMapping("/history-chat")
    public String historyChat() {
        return "customerCare/history-chat";
    }
    @GetMapping("/chatbot-settings")
    public String chatbotSettings() {
        return "customerCare/chatbot-settings";
    }
}
