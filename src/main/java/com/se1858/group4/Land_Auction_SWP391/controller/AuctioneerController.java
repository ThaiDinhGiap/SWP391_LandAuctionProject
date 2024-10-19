package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.entity.Task;
import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import com.se1858.group4.Land_Auction_SWP391.service.AssetService;
import com.se1858.group4.Land_Auction_SWP391.service.TaskService;
import com.se1858.group4.Land_Auction_SWP391.utility.GetSrcInGoogleMapEmbededURLUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/auctioneer")
public class AuctioneerController {
    private TaskService taskService;
    private UserDetailsService userDetailsService;
    private AssetService assetService;
    public AuctioneerController(TaskService taskService, UserDetailsService userDetailsService, AssetService assetService) {
        this.taskService = taskService;
        this.userDetailsService = userDetailsService;
        this.assetService = assetService;
    }
    @GetMapping("/dashboard")
    public String dashboard() {
        return "auctioneer/Dashboard";
    }
    @GetMapping("/awaiting_list")
    public String getAssetAwaitingSchedulingList(Model model) {
        Account auctioneer = userDetailsService.accountAuthenticated();
        List<Task> listTask = taskService.getAllTasksByAuctioneerId(auctioneer.getAccountId(), "In progress");
        model.addAttribute("listTask", listTask);
        return "auctioneer/AssetAwaitingSchedulingList";
    }
    @GetMapping("/viewAssetDetail")
    public String getAssetById(@RequestParam("assetId") int assetId, Model model) {
        Asset asset=assetService.getAssetById(assetId);
        String embedUrl = GetSrcInGoogleMapEmbededURLUtil.extractSrcFromIframe(asset.getCoordinatesOnMap());
        model.addAttribute("embedUrl", embedUrl);
        model.addAttribute("asset",asset);
        return "auctioneer/AssetDetail";
    }
}
