package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.entity.News;
import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import com.se1858.group4.Land_Auction_SWP391.service.AssetService;
import com.se1858.group4.Land_Auction_SWP391.service.NewsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@ControllerAdvice
public class LoginController {

    private final UserDetailsService userDetailsService;
    private final NewsService newsService;
    private final AssetService assetService;

    @Autowired
    public LoginController(UserDetailsService userDetailsService, NewsService newsService, AssetService assetService) {
        this.userDetailsService = userDetailsService;
        this.newsService = newsService;
        this.assetService = assetService;
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
        if (hasRole("ROLE_Customer")) return "redirect:/customer/home";
        if (hasRole("ROLE_Admin")) return "redirect:/admin/localAuthorityList";
        if (hasRole("ROLE_Property_Agent")) return "redirect:/property_agent/dashboard";
        if (hasRole("ROLE_Auctioneer")) return "redirect:/auctioneer/dashboard";
        if (hasRole("ROLE_Customer_Care")) return "redirect:/customer-care/home";
        if (hasRole("ROLE_News_Writer")) return "redirect:/news_writer/dashboard";
        return "redirect:/";
    }

    private boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }

    @GetMapping("/")
    public String hompage(Model model) {
        List<News> top3News = newsService.getTop3LatestNews();
        model.addAttribute("top3LatestNews", top3News);

        List<Asset> top3Assets = assetService.getTop3LastestAssets();
        model.addAttribute("top3Assets", top3Assets);
        return "customer/homepage";
    }

    @GetMapping("/showMyLoginPage")
    public String showLoginPage(@RequestParam(value = "isRegister", required = false) Boolean isRegister,
                                @RequestParam(value = "error", required = false) String error,
                                Model model) {
        model.addAttribute("isRegister", isRegister != null && isRegister);
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password.");
        }
        return "login_register";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "access-denied";
    }

    @GetMapping("/customer/home")
    public String customerHome(Model model) {
        List<News> top3News = newsService.getTop3LatestNews();
        model.addAttribute("top3LatestNews", top3News);

        List<Asset> top3Assets = assetService.getTop3LastestAssets();
        model.addAttribute("top3Assets", top3Assets);
        return "customer/homepage";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/showMyLoginPage";
    }
}
