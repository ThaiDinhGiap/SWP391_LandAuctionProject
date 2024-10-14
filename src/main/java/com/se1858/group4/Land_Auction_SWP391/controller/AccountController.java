package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.BanLog;
import com.se1858.group4.Land_Auction_SWP391.service.AccountService;
import com.se1858.group4.Land_Auction_SWP391.service.BanLogService;
import com.se1858.group4.Land_Auction_SWP391.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/accounts")
public class AccountController {
    AccountService accountService;
    BanLogService banLogService;
    RoleService roleService;

    @Autowired
    public AccountController(AccountService accountService, BanLogService banLogService, RoleService roleService) {
        this.accountService = accountService;
        this.banLogService = banLogService;
        this.roleService = roleService;
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

//    @GetMapping("/list")
//    public String listAccount(Model theModel) {
//
//        // get the employees from db
//        List<Account> accounts = accountService.findALlAccountByStatus("1");
//        // add to the spring model
//        theModel.addAttribute("accounts", accounts);
//        return "account/list-account";
//    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(@ModelAttribute("accounts") Account account, Model theModel ) {
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
        // save the employee
        account.setRegistrationDate(LocalDateTime.now());
        accountService.save(account);
        //use a redirect to prevent duplicate submisstion
        return "redirect:list";
    }

    @PostMapping("/add")
    public String addAccount(@ModelAttribute("accounts") Account account, Model theModel) {
        // Kiểm tra username đã tồn tại chưa
        if (accountService.existsByUsername(account.getUsername())) {
            // Nếu username đã tồn tại, thêm thông báo lỗi vào model
            theModel.addAttribute("errorMessage", "Username already existed. Please using other username.");
            return "account/show-form-account";
        }
        if (accountService.checkEmailExists(account.getEmail())) {
            // Nếu email đã tồn tại, thêm thông báo lỗi vào model
            theModel.addAttribute("errorMessageEmail", "Email already existed. Please using other email.");
            return "account/show-form-account";
        }
        // save the employee
        account.setRegistrationDate(LocalDateTime.now());
        accountService.save(account);
        //use a redirect to prevent duplicate submisstion
        return "redirect:list";
    }

    @PostMapping("/ban")
    public String banAccount(@RequestParam("accountId") int accountId, @RequestParam("reason") String reason) {
        Account account = accountService.findAccountById(accountId);
        // Cập nhật status thành 0 (bị ban)
        account.setStatus(0);

        // Lấy thông tin admin hiện tại từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Lấy đối tượng UserDetails từ Authentication
        User userDetails = (User) authentication.getPrincipal();

        // Lấy tên người dùng từ UserDetails
        String username = userDetails.getUsername();

        // Tìm Account của admin bằng username
        Account admin = accountService.findByUsername(username);

        // Lưu log vào bảng BanLog
        BanLog banLog = new BanLog(admin, account, reason, LocalDateTime.now());
        banLogService.save(banLog);

        accountService.save(account);
        return "redirect:/accounts/list";
    }


}
