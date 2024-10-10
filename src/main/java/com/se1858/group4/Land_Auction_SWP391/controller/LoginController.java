package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@ControllerAdvice
public class LoginController {

    private final UserDetailsService userDetailsService;

    @Autowired
    public LoginController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @ModelAttribute("accountId")
    public void addAccount(Model model) {
        Account account = userDetailsService.accountAuthenticated();
        if (account != null) {
            model.addAttribute("accountId", account.getAccountId());
        }
    }

    @GetMapping("/default")
    public String defaultAfterLogin() {
        if (hasRole("ROLE_Customer")) {
            return "redirect:/customer/home";
        }
        if (hasRole("ROLE_Admin")) {
            return "redirect:/admin/home";
        }
        if (hasRole("ROLE_Property_Agent")) {
            return "redirect:/property-agent/home";
        }
        if (hasRole("ROLE_Autioneer")) {
            return "redirect:/autioneer/home";
        }
        if (hasRole("ROLE_Customer_Care")) {
            return "redirect:/customer-care/home";
        }
        if (hasRole("ROLE_News_Writer")) {
            return "redirect:/news-writer/home";
        }


        return "redirect:/";
    }

    private boolean hasRole(String role) {
        return org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }


    @GetMapping({"/"})
    public String hompage() {
        return "homepage";
    }

    @GetMapping({"/showMyLoginPage"})
    public String showLoginPage() {
        return "showMyLoginPage";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "access-denied";
    }

    @GetMapping("/customer/home")
    public String customerHome() {
        return "customer/homepage_customer";
    }

    @GetMapping("/admin/home")
    public String adminHome() {
        return "dashboardTemplates/dashboard";
    }

    @GetMapping("/customer-care/home")
    public String customerCareHome() {
        return "customerCare/staff-chat";
    }
}
