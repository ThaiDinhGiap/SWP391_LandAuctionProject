package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.Customer;
import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import com.se1858.group4.Land_Auction_SWP391.service.AccountService;
import com.se1858.group4.Land_Auction_SWP391.entity.*;
import com.se1858.group4.Land_Auction_SWP391.service.*;
import com.se1858.group4.Land_Auction_SWP391.utility.GetSrcInGoogleMapEmbededURLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/customer")
public class CustomerController {
    private NewsService newsService;
    private TagForNewsService tagForNewsService;
    private AssetService assetService;
    private TagService tagService;
    private AuctionService auctionService;
    @Autowired
    public CustomerController(NewsService newsService, TagForNewsService tagForNewsService,
                              AssetService assetService, TagService tagService, AuctionService auctionService) {
        this.newsService = newsService;
        this.tagForNewsService = tagForNewsService;
        this.assetService = assetService;
        this.tagService = tagService;
        this.auctionService = auctionService;
    }
    @GetMapping("/get_all_asset")
    public String getAllAsset(Model model) {
        List<Asset> newsList=assetService.getAllAssetWithStatus("Waiting for Auction Scheduling");
        model.addAttribute("listAsset",newsList);
//        lay danh sach cac tag
        List<Tag> tagList=tagService.getAllTag();
        model.addAttribute("listTag",tagList);
        return "customer/assetList";
    }
    @GetMapping("/get_all_auction")
    public String getAllAuction(Model model) {
        List<AuctionSession> auctionList=auctionService.getAllAutcion();
        model.addAttribute("listAuction",auctionList);
        return "customer/auctionList";
    }
    @GetMapping("/get_all_news")
    public String getAllNews(Model model) {
        List<News> newsList=newsService.getAllNews();
        model.addAttribute("listNews",newsList);
//        lay ra 3 bai viet moi nhat
        List<News> top3News=newsService.getTop3LatestNews();
        model.addAttribute("top3LatestNews",top3News);
//        lay danh sach cac tag
        List<TagForNews> tagList=tagForNewsService.getAllTagsForNews();
        model.addAttribute("listTag",tagList);
        return "customer/newsList";
    }
    @GetMapping("/viewNewsDetail")
    public String getNewsById(@RequestParam("newsId") int newsId, Model model) {
        News news=newsService.getNewsById(newsId);
        model.addAttribute("news",news);
//        lay ra 3 bai viet moi nhat
        List<News> top3News=newsService.getTop3LatestNews();
        model.addAttribute("top3LatestNews",top3News);
//        lay danh sach cac tag
        List<TagForNews> tagList=tagForNewsService.getAllTagsForNews();
        model.addAttribute("listTag",tagList);
        return "customer/newsDetail";
    }
    @GetMapping("/viewAssetDetail")
    public String getAssetById(@RequestParam("assetId") int assetId, Model model) {
        Asset asset=assetService.getAssetById(assetId);
        String embedUrl = GetSrcInGoogleMapEmbededURLUtil.extractSrcFromIframe(asset.getCoordinatesOnMap());
        model.addAttribute("embedUrl", embedUrl);
        model.addAttribute("asset",asset);
        return "customer/assetDetail";
    }
    @GetMapping("/viewAuctionDetail")
    public String getAuctionById(@RequestParam("auctionId") int auctionId, Model model) {
        AuctionSession auction=auctionService.getAuctionSessionById(auctionId);
        String embedUrl = GetSrcInGoogleMapEmbededURLUtil.extractSrcFromIframe(auction.getAsset().getCoordinatesOnMap());
        model.addAttribute("embedUrl", embedUrl);
        model.addAttribute("auction",auction);
        return "customer/auctionDetail";
    }
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
