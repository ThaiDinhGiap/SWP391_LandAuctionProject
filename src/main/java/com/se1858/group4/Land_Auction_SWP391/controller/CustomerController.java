package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.Customer;
import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import com.se1858.group4.Land_Auction_SWP391.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Account account = userDetailsService.accountAuthenticated();
        if (account != null) {
            model.addAttribute("account", account);
            model.addAttribute("customer", account.getCustomer());
        }
        return "/customer/profile";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute("customer") Customer customer, @ModelAttribute("account") Account account, Model model) {
        // Update the account and customer information
        accountService.updateAccountDetails(account);
        accountService.updateCustomerDetails(customer);
        return "redirect:/customer/profile"; // redirect back to profile after update
    }
}
