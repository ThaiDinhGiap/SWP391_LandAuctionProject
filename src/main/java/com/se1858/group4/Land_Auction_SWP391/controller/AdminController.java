package com.se1858.group4.Land_Auction_SWP391.controller;


import com.se1858.group4.Land_Auction_SWP391.dto.StaffDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.BanLog;
import com.se1858.group4.Land_Auction_SWP391.entity.Customer;
import com.se1858.group4.Land_Auction_SWP391.entity.LocalAuthority;
import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import com.se1858.group4.Land_Auction_SWP391.service.*;
import com.se1858.group4.Land_Auction_SWP391.utility.FileUploadUtil;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private LocalAuthorityService localAuthorityService;
    private UserDetailsService userDetailsService;
    private FileUploadUtil uploadFile;
    AccountService accountService;
    BanLogService banLogService;
    RoleService roleService;
    CustomerService customerService;

    public AdminController(AccountService accountService, BanLogService banLogService,
                           CustomerService customerService, LocalAuthorityService localAuthorityService,
                           RoleService roleService, FileUploadUtil uploadFile, UserDetailsService userDetailsService) {
        this.accountService = accountService;
        this.banLogService = banLogService;
        this.customerService = customerService;
        this.localAuthorityService = localAuthorityService;
        this.roleService = roleService;
        this.uploadFile = uploadFile;
        this.userDetailsService = userDetailsService;
    }

    //code cua anh Minh
    @GetMapping("/localAuthorityList")
    public String getListOfLocalAuthority(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "localAuthorityName") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String keyword, Model model) {
        Page<LocalAuthority> localAuthorityPage = localAuthorityService.findAllWithPaginationAndSorting(page, size, sortField, sortDir, keyword);
        model.addAttribute("localAuthorityList", localAuthorityPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", localAuthorityPage.getTotalPages());
        model.addAttribute("totalItems", localAuthorityPage.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        return "admin/localAuthorityList";
    }

    @GetMapping("/AddLocalAuthority")
    public String showAddLocalAuthorityForm(Model model) {
        model.addAttribute("authority", new LocalAuthority());
        return "admin/addLocalAuthority";
    }

    @PostMapping("/AddLocalAuthority")
    public String addLocalAuthority(@ModelAttribute("authority") LocalAuthority authority) {
        localAuthorityService.saveAuthority(authority);
        return "redirect:/admin/localAuthorityList";
    }

    @GetMapping("/UpdateLocalAuthority/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        LocalAuthority localAuthority = localAuthorityService.findById(id); // Sửa thành Local_authority
        model.addAttribute("localAuthority", localAuthority);
        return "admin/updateLocalAuthority"; // Tên tệp HTML của trang cập nhật
    }

    @PostMapping("/UpdateLocalAuthority")
    public String updateLocalAuthority(@ModelAttribute LocalAuthority localAuthority) {
        // Tìm authority theo ID và cập nhật các trường
        LocalAuthority existingAuthority = localAuthorityService.findById(localAuthority.getLocalAuthorityId());
        // Cập nhật các trường
        existingAuthority.setLocalAuthorityName(localAuthority.getLocalAuthorityName());
        existingAuthority.setContactPerson(localAuthority.getContactPerson());
        existingAuthority.setPhoneNumber(localAuthority.getPhoneNumber());
        existingAuthority.setEmail(localAuthority.getEmail());
        existingAuthority.setLocalAuthorityAddress(localAuthority.getLocalAuthorityAddress());
        localAuthorityService.saveAuthority(existingAuthority); // Lưu bản cập nhật
        return "redirect:/admin/localAuthorityList"; // Chuyển hướng về trang dashboard
    }

    @GetMapping("/local-profile")
    public String profile(Model model) {
        Account account = userDetailsService.accountAuthenticated();
        if (account != null) {
            StaffDTO staffDTO = new StaffDTO();
            staffDTO.setAccount(account);
            staffDTO.setStaff(account.getStaff());
            model.addAttribute("staffDTO", staffDTO);
        }
        return "admin/profile";
    }

    @PostMapping("/uploadAvatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile avatar, Model model) {
        Account account = userDetailsService.accountAuthenticated();
        if (account != null && avatar != null) {
            uploadFile.UploadAvatar(avatar, account);
        }
        return "redirect:/admin/local-profile";
    }

    @GetMapping("/DeleteLocalAuthority/{id}")
    public String deleteLocalAuthority(@PathVariable("id") Integer id) {
        localAuthorityService.deleteLocalAuthority(id);
        return "redirect:/admin/localAuthorityList";
    }

    //code cua anh Trung
    @GetMapping("/list")
    public String listAccount(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int size,
                              @RequestParam(value = "status", required = false) Integer status,
                              @RequestParam(value = "verify", required = false) Integer verify,
                              Model theModel) {
        Page<Account> accountPage = accountService.findPaginatedWithFilters(page, size, status, verify);
        theModel.addAttribute("accounts", accountPage.getContent());
        theModel.addAttribute("currentPage", page);
        theModel.addAttribute("totalPages", accountPage.getTotalPages());
        theModel.addAttribute("totalItems", accountPage.getTotalElements());
        theModel.addAttribute("statusFilter", status);
        theModel.addAttribute("verifyFilter", verify);
        return "admin/list-account";
    }

    @GetMapping("/listUnverifiedAccount")
    public String listUnverifiedAccount(Model theModel) {
        // get the employees from db
        List<Customer> customers = customerService.findCustomerByUpdateStatus("completed");
        // add to the spring model
        theModel.addAttribute("customers", customers);
        return "admin/list-account-unverify";
    }

    @GetMapping("/showFormForVerify")
    public String showFormForVerify(@RequestParam("customerId") int theId, Model theModel) {
        // get the employee from the service
        Customer customer = customerService.findCustomerByID(theId);
        // set employee in the model to prepopulate the form
        theModel.addAttribute("customer", customer);
        // send over our form
        return "admin/show-form-verify-account";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(@ModelAttribute("accounts") Account account, Model theModel) {
        // create model attribute to bind form data
        Account accounts = new Account();
        theModel.addAttribute("accounts", accounts);
        return "admin/show-form-account";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("accountId") int theId, Model theModel) {
        // get the employee from the service
        Account accounts = accountService.findAccountById(theId);
        // set employee in the model to prepopulate the form
        theModel.addAttribute("accounts", accounts);
        // send over our form
        return "admin/show-form-update";
    }

    @PostMapping("/save")
    public String saveAccount(@ModelAttribute("accounts") Account account, Model theModel) {
        // save the employee
        account.setRegistrationDate(LocalDateTime.now());
        accountService.save(account);
        //use a redirect to prevent duplicate submisstion
        return "redirect:/admin/list";
    }

    @PostMapping("/verifyCustomer")
    public String verifyCustomer(@RequestParam("customerId") int customerId, Model theModel) {
        // Lấy customer dựa vào ID
        Customer customer = customerService.findCustomerByID(customerId);
        if (customer != null) {
            customer.setUpdateStatus("confirmed");
            customerService.save(customer);
        }
        // Dùng redirect để tránh việc submit trùng lặp
        return "redirect:/admin/listUnverifiedAccount";
    }

    @PostMapping("/rejectVerifyCustomer")
    public String rejectVerifyCustomer(@RequestParam("customerId") int customerId, Model theModel) {
        // Lấy customer dựa vào ID
        Customer customer = customerService.findCustomerByID(customerId);
        if (customer != null) {
            customer.setUpdateStatus("incomplete");
            customerService.save(customer);
        }
        // Dùng redirect để tránh việc submit trùng lặp
        return "redirect:/admin/listUnverifiedAccount";
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
            return "admin/show-form-account";
        }
        // save the employee
        account.setRegistrationDate(LocalDateTime.now());
        accountService.add(account);
        //use a redirect to prevent duplicate submisstion
        return "redirect:/admin/list";
    }

    @PostMapping("/ban")
    public String banAccount(@RequestParam("accountId") int accountId, @RequestParam("reason") String reason) {
        Account account = accountService.findAccountById(accountId);
        // Cập nhật status thành 0 (bị ban)
        account.setStatus(0);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Account admin = accountService.findByUsername(username);
        BanLog banLog = new BanLog(admin, account, reason, LocalDateTime.now());
        banLogService.save(banLog);
        accountService.save(account);
        return "redirect:/admin/list";
    }

    @PostMapping("/unBan")
    public String unBanAccount(@RequestParam("accountId") int accountId, @RequestParam("reason") String reason) {
        Account account = accountService.findAccountById(accountId);
        // Cập nhật status thành 0 (bị ban)
        account.setStatus(1);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Account admin = accountService.findByUsername(username);
        BanLog banLog = new BanLog(admin, account, reason, LocalDateTime.now());
        banLogService.save(banLog);
        accountService.save(account);
        return "redirect:/admin/list";
    }

    @PostMapping("/verify")
    public String verifyAccount(@RequestParam("accountId") int accountId) {
        Account account = accountService.findAccountById(accountId);
        // Cập nhật status thành 0 (bị ban)
        account.setVerify(1);
        accountService.save(account);
        return "redirect:/admin/list";
    }

    @GetMapping("/banLog")
    public String listBanLog(Model theModel) {
        // get the employees from db
        List<BanLog> banLogs = banLogService.findAllBanLog();
        // add to the spring model
        theModel.addAttribute("banLogs", banLogs);
        return "admin/list-ban-log";
    }
}
