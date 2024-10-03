package com.se1858.group4.Land_Auction_SWP391.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatBotPageController {

    @GetMapping("/chatbot")
    public String chatbotPage() {
        return "chatbot"; // Trả về tên file HTML chatbot.html
    }
}

