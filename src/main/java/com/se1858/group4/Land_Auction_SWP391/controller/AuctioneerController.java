package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.AuctionSessionDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.entity.AuctionSession;
import com.se1858.group4.Land_Auction_SWP391.entity.Task;
import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import com.se1858.group4.Land_Auction_SWP391.service.AssetService;
import com.se1858.group4.Land_Auction_SWP391.service.AuctionService;
import com.se1858.group4.Land_Auction_SWP391.service.TaskService;
import com.se1858.group4.Land_Auction_SWP391.utility.GetSrcInGoogleMapEmbededURLUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/auctioneer")
public class AuctioneerController {
    private TaskService taskService;
    private UserDetailsService userDetailsService;
    private AssetService assetService;
    private AuctionService auctionService;
    public AuctioneerController(TaskService taskService, UserDetailsService userDetailsService,
                                AssetService assetService, AuctionService auctionService) {
        this.taskService = taskService;
        this.userDetailsService = userDetailsService;
        this.assetService = assetService;
        this.auctionService = auctionService;
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
        AuctionSessionDTO auctionDTO  = new AuctionSessionDTO();
        auctionDTO.setAuctionSession(new AuctionSession());
        auctionDTO.getAuctionSession().setAsset(asset);
        model.addAttribute("auctionDTO",auctionDTO);
        return "auctioneer/AssetDetail";
    }
    @PostMapping("/createAuction")
    public String createAuction(@ModelAttribute("auctionDTO") AuctionSessionDTO auctionDTO) {
        //lay ra auctioneer hien tai
        Account auctioneer = userDetailsService.accountAuthenticated();
        AuctionSession newAuctionSession=new AuctionSession();
        AuctionSession auctionSession=auctionDTO.getAuctionSession();
        newAuctionSession.setAuctionName(auctionSession.getAuctionName());
        newAuctionSession.setStartTime(auctionSession.getStartTime());
        newAuctionSession.setExpectedEndTime(auctionSession.getExpectedEndTime());
        newAuctionSession.setStartingPrice(auctionSession.getStartingPrice());
        newAuctionSession.setMinimumBidIncrement(auctionSession.getMinimumBidIncrement());
        newAuctionSession.setDeposit(auctionSession.getDeposit());
        newAuctionSession.setRegisterFee(auctionSession.getRegisterFee());
        newAuctionSession.setExtraTimeUnit(auctionSession.getExtraTimeUnit());
        newAuctionSession.setStatus("Upcoming");
        newAuctionSession.setRegistrationOpenDate(auctionSession.getRegistrationOpenDate());
        newAuctionSession.setRegistrationCloseDate(auctionSession.getRegistrationCloseDate());
        newAuctionSession.setAuctioneer(auctioneer);
        newAuctionSession.setAsset(auctionSession.getAsset());
        auctionService.createAuctionSession(newAuctionSession);
        return "redirect:/auctioneer/dashboard"; //ve sua lai
    }
}
