package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.HashMap;
import java.util.Map;

import java.security.Principal;
import java.util.NoSuchElementException;

@Controller
public class ChangePasswordController {

    @Autowired
    private AccountService accountService;


    @PostMapping("/profile/change-password")
    @ResponseBody
    public Map<String, String> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Principal principal) {

        Map<String, String> response = new HashMap<>();

        if (!newPassword.equals(confirmPassword)) {
            response.put("status", "error");
            response.put("message", "New password and confirm password do not match.");
            return response;
        }

        String username = principal.getName();

        try {
            accountService.changePassword(username, oldPassword, newPassword);
            response.put("status", "success");
            response.put("message", "Password changed successfully.");
        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        } catch (NoSuchElementException e) {
            response.put("status", "error");
            response.put("message", "User not found.");
        }

        return response;
    }
}
