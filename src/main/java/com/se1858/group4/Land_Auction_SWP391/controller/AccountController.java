package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/accounts")
public class AccountController {
    AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    // add mapping for "/list"

    @GetMapping("/list")
    public String listAccount(Model theModel) {

        // get the employees from db
        List<Account> accounts = accountService.findAllAccount();
        // add to the spring model
        theModel.addAttribute("accounts", accounts);
        return "account/list-account";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        // create model attribute to bind form data
        Account accounts = new Account();
        theModel.addAttribute("accounts", accounts);
        return "account/show-form-account";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("accountId") int theId, Model theModel) {
        // get the employee from the service
        Account accounts = accountService.findAccountById(theId);
        // set employee in the model to prepopulate the form
        theModel.addAttribute("accounts", accounts);
        // send over our form
        return "account/show-form-update";
    }

//    @PutMapping("/update")
//    public ResponseEntity<?> updateAccount(@RequestBody Account updatedAccount, @RequestParam Account adminId, @RequestParam String reason) {
//        accountService.update(updatedAccount, adminId, reason);
//        return ResponseEntity.ok("Account updated and changes logged.");
//    }

    @PostMapping("/save")
    public String saveAccount(@ModelAttribute("accounts") Account account, Model theModel) {
        // Kiểm tra username đã tồn tại chưa
        if (accountService.existsByUsername(account.getUsername())) {
            // Nếu username đã tồn tại, thêm thông báo lỗi vào model
            theModel.addAttribute("errorMessage", "Username already existed. Please enter other username.");
            return "account/show-form-account";
        }

        // save the employee
        account.setRegistrationDate(LocalDateTime.now());
        accountService.save(account);
        //use a redirect to prevent duplicate submisstion
        return "redirect:list";
    }

}
