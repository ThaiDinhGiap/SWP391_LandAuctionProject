package com.se1858.group4.Land_Auction_SWP391.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class ConfigController {

    @Value("${vapid.public.key}")
    private String vapidPublicKey;

    @GetMapping("/api/config")
    public Map<String, String> getConfig() {
        return Map.of("vapidPublicKey", vapidPublicKey);
    }
}

