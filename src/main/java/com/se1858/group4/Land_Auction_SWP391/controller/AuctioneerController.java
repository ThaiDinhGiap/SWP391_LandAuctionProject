package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.AuctionSessionDTO;
import com.se1858.group4.Land_Auction_SWP391.dto.StaffDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.*;
import com.se1858.group4.Land_Auction_SWP391.repository.AccountRepository;
import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import com.se1858.group4.Land_Auction_SWP391.service.*;
import com.se1858.group4.Land_Auction_SWP391.service.AssetService;
import com.se1858.group4.Land_Auction_SWP391.service.AuctionChangeLogService;
import com.se1858.group4.Land_Auction_SWP391.service.AuctionService;
import com.se1858.group4.Land_Auction_SWP391.service.TaskService;
import com.se1858.group4.Land_Auction_SWP391.utility.FileUploadUtil;
import com.se1858.group4.Land_Auction_SWP391.utility.GetSrcInGoogleMapEmbededURLUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedWriter;
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
    private AuctionRegisterService auctionRegisterService;
    private NotificationService notificationService;
    private AccountRepository accountRepository;
    private AccountService accountService;

    public AuctioneerController(TaskService taskService, UserDetailsService userDetailsService,
                                AssetService assetService, AuctionService auctionService,
                                AuctionChangeLogService auctionChangeLogService,
                                AuctionRegisterService auctionRegisterService,
                                NotificationService notificationService,
                                AccountRepository accountRepository,
                                AccountService accountService) {
        this.taskService = taskService;
        this.userDetailsService = userDetailsService;
        this.assetService = assetService;
        this.auctionService = auctionService;
        this.auctionChangeLogService = auctionChangeLogService;
        this.auctionRegisterService = auctionRegisterService;
        this.notificationService = notificationService;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @GetMapping("/get_auction_list")
    public String getAuctionList(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sort", defaultValue = "auctionId") String sort,
            Model model) {

        Account auctioneer = userDetailsService.accountAuthenticated();
        Page<AuctionSession> auctionPage;

        if (search != null && !search.isEmpty()) {
            auctionPage = auctionService.searchAuctionSessionsByAuctioneerIdAndName(auctioneer.getAccountId(), search, PageRequest.of(page, size, Sort.by(sort)));
        } else {
            auctionPage = auctionService.getAllAuctionSessionsByAuctioneerId(auctioneer.getAccountId(), PageRequest.of(page, size, Sort.by(sort)));
        }

        model.addAttribute("listAuction", auctionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", auctionPage.getTotalPages());
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);

        return "auctioneer/AuctionList";
    }


    @GetMapping("/awaiting_list")
    public String getAssetAwaitingSchedulingList(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {

        Account auctioneer = userDetailsService.accountAuthenticated();
        Page<Task> taskPage = taskService.getAllTasksByAuctioneerIdAndSearchAndSort(auctioneer.getAccountId(), "In progress", search, sortDir, page, size);

        model.addAttribute("taskPage", taskPage);
        model.addAttribute("search", search);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", taskPage.getTotalPages());
        return "auctioneer/AssetAwaitingSchedulingList";
    }


    @GetMapping("/viewAssetDetail")
    public String getAssetById(@RequestParam("taskId") int taskId, Model model) {
        if (taskId <= 0) {
            return "redirect:/auctioneer/awaiting_list";
        }
        Task task = taskService.findTaskById(taskId);
        if (task == null) {
            return "redirect:/auctioneer/awaiting_list";
        }
        Account auctioneer = userDetailsService.accountAuthenticated();
        if (task != null && task.getAuctioneer().getAccountId() == auctioneer.getAccountId()) {
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
        } else return "redirect:auctioneer/awaiting_list";
    }

    @GetMapping("/viewAuctionDetail")
    public String getAuctionById(@RequestParam("auctionId") int auctionId, Model model) {
        Account auctioneer = userDetailsService.accountAuthenticated();
        if (auctionId <= 0) {
            return "redirect:/auctioneer/get_auction_list";
        }
        AuctionSession auctionSession = auctionService.getAuctionSessionById(auctionId);
        if (auctionSession == null) {
            return "redirect:/auctioneer/get_auction_list";
        }
        if (auctionSession != null && auctionSession.getAuctioneer().getAccountId() == auctioneer.getAccountId()) {
            List<AuctionChangeLog> listAuctionChangeLog = auctionChangeLogService.getAllAuctionChangeLog(auctionId);
            model.addAttribute("listAuctionChangeLog", listAuctionChangeLog);
            model.addAttribute("auction", auctionSession);
            return "auctioneer/AuctionDetail";
        } else return "redirect:auctioneer/get_auction_list";
    }

    @PostMapping("/createAuction")
    public String createAuction(@ModelAttribute("auctionDTO") AuctionSessionDTO auctionDTO, RedirectAttributes redirectAttributes) {
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
        asset.setAssetStatus("Ongoing");
        auctionService.createAuctionSession(newAuctionSession);
        taskService.changeTaskStatus(auctionDTO.getTaskId(), "Done");
        redirectAttributes.addAttribute("message", "Successful auction scheduling");
        return "redirect:/auctioneer/viewAssetDetail?taskId=" + auctionDTO.getTaskId();
    }

    @GetMapping("/cancelAuctionRequest")
    public String getCancelAuctionForm(@RequestParam("auctionId") int auctionId, Model model) {
        Account auctioneer = userDetailsService.accountAuthenticated();
        if (auctionId <= 0) {
            return "redirect:/auctioneer/get_auction_list";
        }
        AuctionSession auctionSession = auctionService.getAuctionSessionById(auctionId);
        if (auctionSession == null) {
            return "redirect:/auctioneer/get_auction_list";
        }
        if (auctionSession != null && auctionSession.getAuctioneer().getAccountId() == auctioneer.getAccountId()) {
            if (!auctionSession.getStatus().equals("Ending")) {
                model.addAttribute("auctionId", auctionId);
                return "auctioneer/CancelAuction";
            } else return "redirect:/auctioneer/viewAuctionDetail?auctionId=" + auctionId;
        } else return "redirect:/auctioneer/viewAuctionDetail?auctionId=" + auctionId;
    }

    @GetMapping("/updateAuctionRequest")
    public String getUpdateAuctionForm(@RequestParam("auctionId") int auctionId, Model model) {
        Account auctioneer = userDetailsService.accountAuthenticated();
        if (auctionId <= 0) {
            return "redirect:/auctioneer/get_auction_list";
        }
        AuctionSession auction = auctionService.getAuctionSessionById(auctionId);
        if (auction == null) {
            return "redirect:/auctioneer/get_auction_list";
        }
        if (auction != null && auction.getAuctioneer().getAccountId() == auctioneer.getAccountId()) {
            if (auction.getStatus().equals("Upcoming") && LocalDateTime.now().isBefore(auction.getRegistrationOpenDate())) {
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
            } else return "redirect:/auctioneer/viewAuctionDetail?auctionId=" + auctionId;
        } else return "redirect:/auctioneer/viewAuctionDetail?auctionId=" + auctionId;
    }

    @PostMapping("/updateAuction")
    public String updateAuction(@ModelAttribute("auctionDTO") AuctionSessionDTO auctionDTO,
                                @RequestParam("start_time") LocalDateTime start_time,
                                @RequestParam("expected_end_time") LocalDateTime expected_end_time,
                                @RequestParam("registration_open_date") LocalDateTime registration_open_date,
                                @RequestParam("registration_close_date") LocalDateTime registration_close_date,
                                @RequestParam("reason") String reason) {
        Account auctioneer = userDetailsService.accountAuthenticated();
        if (auctionDTO != null && auctionDTO.getAuctionSession().getAuctioneer().getAccountId() == auctioneer.getAccountId()) {
            auctionDTO.getAuctionSession().setStartTime(start_time);
            auctionDTO.getAuctionSession().setExpectedEndTime(expected_end_time);
            auctionDTO.getAuctionSession().setRegistrationOpenDate(registration_open_date);
            auctionDTO.getAuctionSession().setRegistrationCloseDate(registration_close_date);
            auctionService.updateAuctionSession(auctionDTO);
            auctionChangeLogService.createAuctionChange("Update auction information", reason, auctionDTO.getAuctionSession().getAuctionId());
        }
        return "redirect:/auctioneer/viewAuctionDetail?auctionId=" + auctionDTO.getAuctionSession().getAuctionId();
    }

    @PostMapping("/cancelAuction")
    public String cancelAuction(@RequestParam("auctionId") int auctionId, @RequestParam("reason") String reason) {
        Account auctioneer = userDetailsService.accountAuthenticated();
        if (auctionId <= 0) {
            return "redirect:/auctioneer/get_auction_list";
        }
        AuctionSession auctionSession = auctionService.getAuctionSessionById(auctionId);
        if (auctionSession == null) {
            return "redirect:/auctioneer/get_auction_list";
        }
        if (auctionSession != null && auctionSession.getAuctioneer().getAccountId() == auctioneer.getAccountId()) {
            auctionService.cancelAuction(auctionId);
            Asset asset = auctionSession.getAsset();
            asset.setAssetStatus("Failed sold");
            assetService.updateAsset(asset);
            auctionChangeLogService.createAuctionChange("Cancelled auction session", reason, auctionId);
        }
        return "redirect:/auctioneer/viewAuctionDetail?auctionId=" + auctionId;
    }

    @GetMapping("/viewRegisterList")
    public String getRegisterList(@RequestParam("auctionId") int auctionId, Model model) {
        Account auctioneer = userDetailsService.accountAuthenticated();
        if (auctionId <= 0) {
            return "redirect:/auctioneer/get_auction_list";
        }
        AuctionSession auctionSession = auctionService.getAuctionSessionById(auctionId);
        if (auctionSession == null) {
            return "redirect:/auctioneer/get_auction_list";
        }
        if (auctionSession != null && auctionSession.getAuctioneer().getAccountId() == auctioneer.getAccountId()) {
            List<AuctionRegister> registerList = auctionRegisterService.getAllAuctionRegistersByAuctionId(auctionId);
            model.addAttribute("registerList", registerList);
            return "auctioneer/RegisterList";
        } else return "redirect:/auctioneer/viewAuctionDetail?auctionId=" + auctionId;
    }

    @GetMapping("/viewResult")
    public String getResult(@RequestParam("auctionId") int auctionId, Model model) {
        Account auctioneer = userDetailsService.accountAuthenticated();
        if (auctionId <= 0) {
            return "redirect:/auctioneer/get_auction_list";
        }
        AuctionSession auctionSession = auctionService.getAuctionSessionById(auctionId);
        if (auctionSession == null) {
            return "redirect:/auctioneer/get_auction_list";
        }
        if (auctionSession != null && auctionSession.getAuctioneer().getAccountId() == auctioneer.getAccountId()) {
            List<AuctionRegister> registerList = auctionRegisterService.resultOfAuction(auctionId, "Confirmed");
            model.addAttribute("currentAuction", auctionSession);
            model.addAttribute("auctionId", auctionId);
            model.addAttribute("registerList", registerList);
            return "auctioneer/Result";
        } else return "redirect:/auctioneer/viewAuctionDetail?auctionId=" + auctionId;
    }


    @GetMapping("/viewRegisterDetail")
    public String getRegisterDetail(@RequestParam("registerId") int registerId, Model model) {
        Account auctioneer = userDetailsService.accountAuthenticated();
        if (registerId <= 0) {
            return "redirect:/auctioneer/get_auction_list";
        }
        AuctionRegister register = auctionRegisterService.getAuctionRegisterById(registerId);
        if (register == null) {
            return "redirect:/auctioneer/get_auction_list";
        }
        if (register != null && register.getAuction().getAuctioneer().getAccountId() == auctioneer.getAccountId()) {
            model.addAttribute("register", register);
            return "auctioneer/RegisterDetail";
        } else
            return "redirect:/auctioneer/viewAuctionDetail?auctionId=" + register.getAuction().getAuctioneer().getAccountId();
    }

    @PostMapping("/sendScheduleMail")
    public ResponseEntity<String> sendScheduleMail(@RequestParam("registerId") int registerId){
        accountService.sendScheduleMail(registerId);
        return ResponseEntity.ok("Schedule sent successfully!");
    }

    @PostMapping("/cancelRight")
    public ResponseEntity<String> cancelRight(@RequestParam("registerId") int registerId){
        accountService.cancelRight(registerId);
        return ResponseEntity.ok("Cancel right successfully!");
    }

    @PostMapping("/markAsSuccessfulSold")
    public ResponseEntity<String> successfulSold(@RequestParam("auctionId") int auctionId){
        AuctionSession auctionSession = auctionService.getAuctionSessionById(auctionId);
        assetService.setSuccessSoldForAsset(auctionSession.getAsset());
        return ResponseEntity.ok("Sold asset successfully!");
    }

    @PostMapping("/markAsFailedSold")
    public ResponseEntity<String> failedSold(@RequestParam("auctionId") int auctionId){
        AuctionSession auctionSession = auctionService.getAuctionSessionById(auctionId);
        assetService.setFailedSoldForAsset(auctionSession.getAsset());
        return ResponseEntity.ok("Sold asset failed!");
    }

    @PostMapping("/updateRegister")
    public String updateRegister(@RequestParam("registerId") int registerId, @RequestParam("register_status") String register_status,
                                 @RequestParam("purchase_status") String purchase_status, @RequestParam("deposit_status") String deposit_status) {
        if (registerId <= 0) {
            return "redirect:/auctioneer/get_auction_list";
        }
        AuctionRegister register = auctionRegisterService.getAuctionRegisterById(registerId);
        if (register == null) {
            return "redirect:/auctioneer/get_auction_list";
        }
        Account auctioneer = userDetailsService.accountAuthenticated();
        if (register != null && register.getAuction().getAuctioneer().getAccountId() == auctioneer.getAccountId()) {
            if (register_status != null) {
                if (register_status.equals("Confirmed")) {
                    Notification notification = new Notification();
                    notification.setContent("Auction registration confirmed successfully!");
                    notification.setCreatedDate(LocalDateTime.now());
                    notification.setReadStatus("unread");
                    notification.setAuction(register.getAuction());

                    Account buyer = accountRepository.findById(register.getBuyer().getAccountId())
                            .orElseThrow(() -> new RuntimeException("Account not found"));

                    notification.addAccount(buyer);
                    notificationService.saveNotification(notification);
                    buyer.addNotification(notification);
                    accountRepository.save(buyer);
                    notificationService.sendNotification(notification);
                }
                register.setRegisterStatus(register_status);
            }
            if (purchase_status.isEmpty()) {
                register.setPurchaseStatus(null);
            } else register.setPurchaseStatus(purchase_status);
            if (deposit_status.isEmpty()) {
                register.setDepositStatus(null);
            } else register.setDepositStatus(deposit_status);
            auctionRegisterService.updateRegisterStatus(register);
            return "redirect:/auctioneer/viewRegisterDetail?registerId=" + registerId;
        } else
            return "redirect:/auctioneer/viewAuctionDetail?auctionId=" + register.getAuction().getAuctioneer().getAccountId();
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
        if (account != null && avatar != null) {
            uploadFile.UploadAvatar(avatar, account);
        }
        return "redirect:/auctioneer/profile";
    }
}
