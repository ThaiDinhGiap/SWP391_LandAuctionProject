package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.Local_authority;
import com.se1858.group4.Land_Auction_SWP391.service.LocalAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LocalAuthorityController {

    @Autowired
    private LocalAuthorityService localAuthorityService;

    @GetMapping("/local-authorities")
    public String getAllLocalAuthorities(Model model) {
        List<Local_authority> authorities = localAuthorityService.getAllLocalAuthorities();
        model.addAttribute("authorities", authorities);
        return "dashboard"; // Tên view
    }

}
