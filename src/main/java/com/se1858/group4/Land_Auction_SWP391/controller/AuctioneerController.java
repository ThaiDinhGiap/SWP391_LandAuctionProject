package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.AuctionSessionDTO;
import com.se1858.group4.Land_Auction_SWP391.dto.StaffDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.*;
import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import com.se1858.group4.Land_Auction_SWP391.service.AssetService;
import com.se1858.group4.Land_Auction_SWP391.service.AuctionChangeLogService;
import com.se1858.group4.Land_Auction_SWP391.service.AuctionService;
import com.se1858.group4.Land_Auction_SWP391.service.TaskService;
import com.se1858.group4.Land_Auction_SWP391.utility.FileUploadUtil;
import com.se1858.group4.Land_Auction_SWP391.utility.GetSrcInGoogleMapEmbededURLUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/auctioneer")
public class AuctioneerController {
    private FileUploadUtil uploadFile;
    private TaskService taskService;
    private UserDetailsService userDetailsService;
    private AssetService assetService;
    private AuctionService auctionService;
    private AuctionChangeLogService auctionChangeLogService;


    public AuctioneerController(TaskService taskService, UserDetailsService userDetailsService,
                                AssetService assetService, AuctionService auctionService,
                                AuctionChangeLogService auctionChangeLogService) {
        this.taskService = taskService;
        this.userDetailsService = userDetailsService;
        this.assetService = assetService;
        this.auctionService = auctionService;
        this.auctionChangeLogService = auctionChangeLogService;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "auctioneer/Dashboard";
    }

    @GetMapping("/get_auction_list")
    public String getAuctionList(Model model) {
        Account auctioneer = userDetailsService.accountAuthenticated();
        List<AuctionSession> list = auctionService.getAllAuctionSessionsByAuctioneerId(auctioneer.getAccountId());
        model.addAttribute("listAuction", list);
        return "auctioneer/AuctionList";
    }

    @GetMapping("/awaiting_list")
    public String getAssetAwaitingSchedulingList(Model model) {
        Account auctioneer = userDetailsService.accountAuthenticated();
        List<Task> listTask = taskService.getAllTasksByAuctioneerId(auctioneer.getAccountId(), "In progress");
        model.addAttribute("listTask", listTask);
        return "auctioneer/AssetAwaitingSchedulingList";
    }

    @GetMapping("/viewAssetDetail")
    public String getAssetById(@RequestParam("taskId") int taskId, Model model) {
        Task task = taskService.findTaskById(taskId);
        Asset asset = task.getAsset();
        String embedUrl = GetSrcInGoogleMapEmbededURLUtil.extractSrcFromIframe(asset.getCoordinatesOnMap());
        model.addAttribute("embedUrl", embedUrl);
        model.addAttribute("asset", asset);
        AuctionSessionDTO auctionDTO = new AuctionSessionDTO();
        auctionDTO.setAuctionSession(new AuctionSession());
        auctionDTO.setTaskId(task.getTaskId());
        auctionDTO.setAssetId(asset.getAssetId());
        model.addAttribute("auctionDTO", auctionDTO);
        return "auctioneer/AssetDetail";
    }

    @GetMapping("/viewAuctionDetail")
    public String getAuctionById(@RequestParam("auctionId") int auctionId, Model model) {
        AuctionSession aucitonSession = auctionService.getAuctionSessionById(auctionId);
        List<AuctionChangeLog> listAuctionChangeLog = auctionChangeLogService.getAllAuctionChangeLog(auctionId);
        model.addAttribute("listAuctionChangeLog", listAuctionChangeLog);
        model.addAttribute("auction", aucitonSession);
        return "auctioneer/AuctionDetail";
    }

    @GetMapping("/cancelAuctionRequest")
    public String getCancelAuctionForm(@RequestParam("auctionId") int auctionId, Model model) {
        AuctionSession auctionSession = auctionService.getAuctionSessionById(auctionId);
        if(auctionSession!=null && !auctionSession.getStatus().equals("Cancelled")){
            model.addAttribute("auctionId", auctionId);
            return "auctioneer/CancelAuction";
        }
        else return "redirect:/auctioneer/viewAuctionDetail?auctionId=" + auctionId;
    }

    @GetMapping("/updateAuctionRequest")
    public String getUpdateAuctionForm(@RequestParam("auctionId") int auctionId, Model model) {
        AuctionSession auction = auctionService.getAuctionSessionById(auctionId);
        if(auction!=null && auction.getStatus().equals("Upcoming") && LocalDateTime.now().isBefore(auction.getRegistrationOpenDate())){
            AuctionSessionDTO auctionDTO = new AuctionSessionDTO();
            auctionDTO.setAuctionSession(auction);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            String formattedStartTime = auction.getStartTime().format(formatter);
            String formattedExpectedEndTime = auction.getExpectedEndTime().format(formatter);
            String formattedRegistrationOpenDate = auction.getRegistrationOpenDate().format(formatter);
            String formattedRegistrationCloseDate = auction.getRegistrationCloseDate().format(formatter);
            model.addAttribute("auctionDTO", auctionDTO);
            model.addAttribute("formattedStartTime", formattedStartTime);
            model.addAttribute("formattedExpectedEndTime", formattedExpectedEndTime);
            model.addAttribute("formattedRegistrationOpenDate", formattedRegistrationOpenDate);
            model.addAttribute("formattedRegistrationCloseDate", formattedRegistrationCloseDate);
            return "auctioneer/UpdateAuction";
        }
        else return "redirect:/auctioneer/viewAuctionDetail?auctionId=" + auctionId;
    }

    @PostMapping("/createAuction")
    public String createAuction(@ModelAttribute("auctionDTO") AuctionSessionDTO auctionDTO) {
        //lay ra auctioneer hien tai
        Account auctioneer = userDetailsService.accountAuthenticated();
        AuctionSession newAuctionSession = new AuctionSession();
        AuctionSession auctionSession = auctionDTO.getAuctionSession();
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
        Asset asset = assetService.getAssetById(auctionDTO.getAssetId());
        newAuctionSession.setAsset(asset);
        auctionService.createAuctionSession(newAuctionSession);
        taskService.changeTaskStatus(auctionDTO.getTaskId(), "Done");
        return "redirect:/auctioneer/dashboard";
    }

    @PostMapping("/updateAuction")
    public String updateAuction(@ModelAttribute("auctionDTO") AuctionSessionDTO auctionDTO,
                                @RequestParam("start_time") LocalDateTime start_time,
                                @RequestParam("expected_end_time") LocalDateTime expected_end_time,
                                @RequestParam("registration_open_date") LocalDateTime registration_open_date,
                                @RequestParam("registration_close_date") LocalDateTime registration_close_date,
                                @RequestParam("reason") String reason) {
        auctionDTO.getAuctionSession().setStartTime(start_time);
        auctionDTO.getAuctionSession().setExpectedEndTime(expected_end_time);
        auctionDTO.getAuctionSession().setRegistrationOpenDate(registration_open_date);
        auctionDTO.getAuctionSession().setRegistrationCloseDate(registration_close_date);
        auctionService.updateAuctionSession(auctionDTO);
        auctionChangeLogService.createAuctionChange("Update auction information", reason, auctionDTO.getAuctionSession().getAuctionId());
        return "redirect:/auctioneer/viewAuctionDetail?auctionId=" + auctionDTO.getAuctionSession().getAuctionId();
    }
    @PostMapping("/cancelAuction")
    public String cancelAuction(@RequestParam("auctionId") int auctionId, @RequestParam("reason") String reason) {
        auctionService.cancelAuction(auctionId);
        auctionChangeLogService.createAuctionChange("Cancelled auction information", reason, auctionId);
        return "redirect:/auctioneer/viewAuctionDetail?auctionId=" + auctionId;
    }


    @GetMapping("/profile")
    public String profile(Model model) {
        Account account = userDetailsService.accountAuthenticated();
        if (account != null) {
            StaffDTO staffDTO = new StaffDTO();
            staffDTO.setAccount(account);
            staffDTO.setStaff(account.getStaff());
            model.addAttribute("staffDTO", staffDTO);
        }
        return "auctioneer/profile";
    }
    @PostMapping("/uploadAvatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile avatar, Model model) {
        Account account = userDetailsService.accountAuthenticated();
        if (account != null&&avatar!=null) {
            uploadFile.UploadAvatar(avatar, account);
        }
        return "redirect:/auctioneer/profile";
    }
}
