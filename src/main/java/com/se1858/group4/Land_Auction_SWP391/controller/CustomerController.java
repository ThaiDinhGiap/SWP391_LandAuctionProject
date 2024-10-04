package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.Customer;
import com.se1858.group4.Land_Auction_SWP391.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/customer")
public class CustomerController {

//    @Autowired
//    private AccountService accountService;
//
//    @GetMapping("/account-info")
//    public String getAccountInfo(Model model, @RequestParam("accountId") int accountId) {
//        Account account = accountService.findAccountByAccountId(accountId);
//        Customer customer = accountService.findCustomerByAccountId(accountId);
//        model.addAttribute("account", account);
//        model.addAttribute("customer", customer);
//        return "account-info";  // Render account-info.html
//    }
}
