package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.LocalAuthority;
import com.se1858.group4.Land_Auction_SWP391.service.LocalAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/local-authorities")
public class LocalAuthorityController {

    @Autowired
    private LocalAuthorityService localAuthorityService;

    @GetMapping
    public List<LocalAuthority> getAllLocalAuthorities() {
        return localAuthorityService.findAll();
    }
}
