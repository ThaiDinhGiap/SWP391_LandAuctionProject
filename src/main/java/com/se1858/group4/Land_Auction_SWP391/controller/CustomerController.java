package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.CustomerDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.Customer;
import com.se1858.group4.Land_Auction_SWP391.entity.Image;
import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import com.se1858.group4.Land_Auction_SWP391.service.AccountService;
import com.se1858.group4.Land_Auction_SWP391.entity.*;
import com.se1858.group4.Land_Auction_SWP391.service.*;
import com.se1858.group4.Land_Auction_SWP391.utility.GetSrcInGoogleMapEmbededURLUtil;
import com.se1858.group4.Land_Auction_SWP391.service.CustomerService;
import com.se1858.group4.Land_Auction_SWP391.service.ImageService;
import com.se1858.group4.Land_Auction_SWP391.utility.FileUploadUtil;
import com.se1858.group4.Land_Auction_SWP391.utility.QrCode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
@RequestMapping("/customer")
public class CustomerController {
    private NewsService newsService;
    private TagForNewsService tagForNewsService;
    private AssetService assetService;
    private TagService tagService;
    private AuctionService auctionService;
    private UserDetailsService userDetailsService;
    private AccountService accountService;
    private FileUploadUtil uploadFile;
    private CustomerService customerService;
    private QrCode qrCode;
    private AuctionRegisterService auctionRegisterService;
    private NotificationService notificationService;
    private BidService bidService;

    public CustomerController(NewsService newsService, TagForNewsService tagForNewsService,
                              AssetService assetService, TagService tagService, AuctionService auctionService,
                              UserDetailsService userDetailsService, AccountService accountService,
                              FileUploadUtil uploadFile,
                              CustomerService customerService, QrCode qrCode, AuctionRegisterService auctionRegisterService,
                              NotificationService notificationService, BidService bidService) {
        this.newsService = newsService;
        this.tagForNewsService = tagForNewsService;
        this.assetService = assetService;
        this.tagService = tagService;
        this.auctionService = auctionService;
        this.userDetailsService = userDetailsService;
        this.accountService = accountService;
        this.uploadFile = uploadFile;
        this.customerService = customerService;
        this.qrCode = qrCode;
        this.auctionRegisterService = auctionRegisterService;
        this.notificationService = notificationService;
        this.bidService = bidService;
    }

    @GetMapping("/viewAuctionHistory")
    public String getAuctionHistory(Model model) {
        Account this_user = userDetailsService.accountAuthenticated();
        List<AuctionRegister> registerList = auctionRegisterService.getAllAuctionRegistersByAccountId(this_user.getAccountId());
        model.addAttribute("registerList", registerList);
        return "customer/auctionHistory";
    }

    @GetMapping("/get_all_asset")
    public String getAllAsset(Model model) {
        //lay ra toan bo tai san
        List<Asset> newsList = assetService.getAllAsset();
        model.addAttribute("listAsset", newsList);
//        lay danh sach cac tag
        List<Tag> tagList = tagService.getAllTag();
        model.addAttribute("listTag", tagList);
        return "customer/assetList";
    }


    @GetMapping("/get_all_auction")
    public String getAllAuction(Model model) {
        List<AuctionSession> auctionList = auctionService.getAllAutcion();
        model.addAttribute("listAuction", auctionList);
        return "customer/auctionList";
    }

    @GetMapping("/aboutus")
    public String aboutUs() {
        return "customer/about";
    }

    @GetMapping("/get_all_news")
    public String getAllNews(Model model) {
        List<News> newsList = newsService.getAllNews();
        model.addAttribute("listNews", newsList);
//        lay ra 3 bai viet moi nhat
        List<News> top3News = newsService.getTop3LatestNews();
        model.addAttribute("top3LatestNews", top3News);
//        lay danh sach cac tag
        List<TagForNews> tagList = tagForNewsService.getAllTagsForNews();
        model.addAttribute("listTag", tagList);
        return "customer/newsList";
    }

    @GetMapping("/filter_assets")
    public String filterAssets(
            @RequestParam(required = false) List<Integer> tagIds,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Model model) {

        List<Asset> filteredAssets = assetService.filterAssets(tagIds, keyword, fromDate, toDate);
        model.addAttribute("listAsset", filteredAssets);
        return "customer/assetList :: assetListFragment";
    }

    @GetMapping("/filter_auctions")
    public String filterAuctions(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String status,
            Model model) {

        List<AuctionSession> filteredAuctions = auctionService.filterAuctionSessions(keyword, fromDate, toDate, status);
        model.addAttribute("listAuction", filteredAuctions);
        return "customer/auctionList :: auctionListFragment";
    }

    @GetMapping("/filter_news")
    public String filterNews(
            @RequestParam(required = false) List<Integer> tagIds,
            @RequestParam(required = false) String keyword,
            Model model) {
        List<News> filteredNews = newsService.filterNews(tagIds, keyword);
        model.addAttribute("listNews", filteredNews);
        return "customer/newsList :: newsListFragment";
    }

    @GetMapping("/viewNewsDetail")
    public String getNewsById(@RequestParam("newsId") int newsId, Model model) {
        if (newsId <= 0) {
            return "redirect:/customer/get_all_news";
        }
        News news = newsService.getNewsById(newsId);
        if (news == null) {
            return "redirect:/customer/get_all_news";
        }
        model.addAttribute("news", news);
//        lay ra 3 bai viet moi nhat
        List<News> top3News = newsService.getTop3LatestNews();
        model.addAttribute("top3LatestNews", top3News);
//        lay danh sach cac tag
        List<TagForNews> tagList = tagForNewsService.getAllTagsForNews();
        model.addAttribute("listTag", tagList);
        return "customer/newsDetail";
    }

    @GetMapping("/viewAssetDetail")
    public String getAssetById(@RequestParam("assetId") int assetId, Model model) {
        if (assetId <= 0) {
            return "redirect:/customer/get_all_asset";
        }
        Asset asset = assetService.getAssetById(assetId);
        if (asset == null) {
            return "redirect:/customer/get_all_asset";
        }
        String embedUrl = GetSrcInGoogleMapEmbededURLUtil.extractSrcFromIframe(asset.getCoordinatesOnMap());
        model.addAttribute("embedUrl", embedUrl);
        model.addAttribute("asset", asset);
        return "customer/assetDetail";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Account account = userDetailsService.accountAuthenticated();
        if (account != null) {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setAccount(account);
            customerDTO.setCustomer(account.getCustomer());
            model.addAttribute("customerDTO", customerDTO);
        }
        return "/customer/profile";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(
            @ModelAttribute("customerDTO") CustomerDTO customerDTO,
            @RequestParam("idCardFrontImage") MultipartFile idCardFrontImage,
            @RequestParam("idCardBackImage") MultipartFile idCardBackImage, Model model) {
        // Extract customer and account from DTO
        Customer customer = customerDTO.getCustomer();
        Account account = customerDTO.getAccount();
        // Check if account and customer are not null
        if (account != null && customer != null) {
            // Update account and customer details
            accountService.updateAccountDetails(account);
            customerService.updateCustomerDetails(customer);
            // Handle file uploads
            if (!idCardFrontImage.isEmpty() || !idCardBackImage.isEmpty()) {
                uploadFile.UploadImagesForCustomer(idCardFrontImage, idCardBackImage, customer);
            }
        } else {
            // Handle the case where account or customer is null
            model.addAttribute("errorMessage", "Account or Customer data is missing.");
            return "/customer/profile";
        }
        return "redirect:/customer/profile";
    }

    @PostMapping("/uploadAvatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile avatar, Model model) {
        Account account = userDetailsService.accountAuthenticated();
        if (account != null && avatar != null) {
            uploadFile.UploadAvatar(avatar, account);
        }
        return "redirect:/customer/profile";
    }

    @GetMapping("/viewAuctionDetail")
    public String getAuctionById(@RequestParam(value = "error", required = false) String error, @RequestParam("auctionId") int auctionId, Model model) {
        if (auctionId <= 0) {
            return "redirect:/customer/get_all_auction";
        }
        AuctionSession auction = auctionService.getAuctionSessionById(auctionId);
        if (auction == null) {
            return "redirect:/customer/get_all_auction";
        }
        //lay ra nguoi dang ky
        Account this_user = userDetailsService.accountAuthenticated();
        String embedUrl = GetSrcInGoogleMapEmbededURLUtil.extractSrcFromIframe(auction.getAsset().getCoordinatesOnMap());
        model.addAttribute("embedUrl", embedUrl);
        model.addAttribute("auction", auction);
        qrCode.setAmount(auction.getDeposit() + auction.getRegisterFee() + "");
        qrCode.setDescription("UserId " + this_user.getAccountId() + " deposit fee AuctionId " + auction.getAuctionId());
        model.addAttribute("qrCode", qrCode);
        AuctionRegister register = auctionRegisterService.getAuctionRegister(auctionId, this_user.getAccountId());
        if (register != null) {
            model.addAttribute("auction_register", register);
        }
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "customer/auctionDetail";
    }

    @GetMapping("/joinAuctionDetail")
    public String accessAuction(@RequestParam String auctionId, Model model) {
        // Truy vấn Account now
        int auctionIdUsing = Integer.parseInt(auctionId);
        Account currentAccount = userDetailsService.accountAuthenticated();

        if (currentAccount == null) {
            return getAuctionById("User account not found.", auctionIdUsing, model);
        }

        // Kiểm tra xem người dùng có được phép truy cập phiên đấu giá không
        if (!auctionService.isUserAllowedToAccessAuction(auctionIdUsing, currentAccount.getAccountId())) {
            return getAuctionById("You are not allowed to access this auction.", auctionIdUsing, model);
        }

        // Lấy thông tin phiên đấu giá
        AuctionSession auctionSession = auctionService.getAuctionSessionById(auctionIdUsing);
        List<Bid> bidList = bidService.getAllBidsByAuctionId(auctionIdUsing);

        LocalDateTime now = LocalDateTime.now();

        // Kiểm tra thời gian của phiên đấu giá
        if (now.isBefore(auctionSession.getStartTime())) {
            return getAuctionById("The auction has not started yet.", auctionIdUsing, model);
        } else if (auctionSession.getActualEndTime() != null && now.isAfter(auctionSession.getActualEndTime())) {
            return getAuctionById("The auction has already ended.", auctionIdUsing, model);
        }

        AuctionSession auction = auctionService.getAuctionSessionById(auctionIdUsing);
        String embedUrl = GetSrcInGoogleMapEmbededURLUtil.extractSrcFromIframe(auction.getAsset().getCoordinatesOnMap());
        model.addAttribute("embedUrl", embedUrl);
        model.addAttribute("auction", auction);

        // Truyền dữ liệu phiên đấu giá đến View
        model.addAttribute("auctionSession", auctionSession);
        model.addAttribute("accountCustomer", currentAccount);
        model.addAttribute("bidList", bidList);

        // Trả về tên của view đấu giá
        return "customer/auctionPage";
    }

    @PostMapping("/registerAuction")
    public String registerAuction(@RequestParam(value = "validate", required = false) String validate,
                                  @RequestParam("auctionId") int auctionId, RedirectAttributes redirectAttributes) {
        if (auctionId <= 0) {
            return "redirect:/customer/get_all_auction";
        }
        AuctionSession auction = auctionService.getAuctionSessionById(auctionId);
        if (auction == null) {
            return "redirect:/customer/get_all_auction";
        }
        //check xem nguoi dung da validate tai khoan chua
        Account this_user = userDetailsService.accountAuthenticated();
        // Kiểm tra tài khoản đã xác thực chưa
        if (this_user.getVerify() == 1) {
            if (validate != null) {
                // Cập nhật trạng thái đăng ký vào database
                AuctionRegister register = new AuctionRegister(auction, this_user, "Waiting for payment", null, null, null, LocalDateTime.now());
                auctionRegisterService.createAuctionRegister(register);

                // Tạo thông báo sau khi đăng ký thành công
                Notification notification = new Notification();
                notification.setContent("You have registered to participate in the auction, please transfer money to complete the procedure.");
                notification.setCreatedDate(LocalDateTime.now());
                notification.setReadStatus("unread"); // Trạng thái chưa đọc

                // Lưu thông báo vào cơ sở dữ liệu và gửi SSE cho client
                notification.addAccount(this_user);
                this_user.addNotification(notification);
                notificationService.saveNotification(notification);
                notificationService.sendNotification(notification); // Gửi SSE tới client

                redirectAttributes.addFlashAttribute("error", "Registration successful, please transfer the deposit and register fee");
            }
        } else {
            // Tài khoản chưa xác thực
            redirectAttributes.addFlashAttribute("error", "Please complete your personal information before registering for the auction");
            if (LocalDateTime.now().isAfter(auction.getRegistrationOpenDate()) && LocalDateTime.now().isBefore(auction.getRegistrationCloseDate())) {
                if (this_user.getVerify() == 1) {
                    //kiem tra xem nguoi dung da tick het chua
                    if (validate != null) {
                        //cap nhat trang thai vao database
                        AuctionRegister register = new AuctionRegister(auction, this_user, "Waiting for payment", null, null, null, LocalDateTime.now());
                        auctionRegisterService.createAuctionRegister(register);
                        redirectAttributes.addFlashAttribute("error", "Registration successful, please transfer the deposit and register fee");
                    }
                } else {
                    //gui thong bao tai khoan chua validate
                    redirectAttributes.addFlashAttribute("error", "Please complete your personal information before registering for the auction");
                }
            }
        }

        return "redirect:/customer/viewAuctionDetail?auctionId=" + auctionId;
    }


    @PostMapping("/transferDepositAndFee")
    public String transferDepositAndFee(@RequestParam(value = "transfer", required = false) String transfer,
                                        @RequestParam("auctionId") int auctionId,
                                        @RequestParam("auctionRegisterId") int auctionRegisterId, RedirectAttributes redirectAttributes) {
        if (auctionId <= 0) {
            return "redirect:/customer/get_all_auction";
        }
        AuctionSession auction = auctionService.getAuctionSessionById(auctionId);
        if (auction == null) {
            return "redirect:/customer/get_all_auction";
        }
        if (LocalDateTime.now().isAfter(auction.getRegistrationOpenDate()) && LocalDateTime.now().isBefore(auction.getRegistrationCloseDate())) {
            //check xem nguoi dung da chuyen tien chua
            if (transfer != null) {
                AuctionRegister auctionRegister = auctionRegisterService.getAuctionRegisterById(auctionRegisterId);
                auctionRegister.setRegisterStatus("Waiting for confirmation");
                auctionRegisterService.updateRegisterStatus(auctionRegister);
                redirectAttributes.addFlashAttribute("error", "Please wait while we confirm the transaction, the result will be sent to you via notification");
            } else {
                redirectAttributes.addFlashAttribute("error", "Please make sure to transfer the deposit and register fee before the auction registration deadline");
            }
            Account this_user = userDetailsService.accountAuthenticated();
            //check xem nguoi dung da chuyen tien chua
            if (transfer != null) {
                AuctionRegister auctionRegister = auctionRegisterService.getAuctionRegisterById(auctionRegisterId);
                auctionRegister.setRegisterStatus("dang cho xac nhan chuyen tien");
                auctionRegisterService.updateRegisterStatus(auctionRegister);
                redirectAttributes.addFlashAttribute("error", "Please wait while we confirm the transaction, the result will be sent to you via notification");

                // Tạo thông báo sau khi đăng ký thành công
                Notification notification = new Notification();
                notification.setContent("You have transfer deposit and fee. Please wait while we confirm the transaction, the result will be sent to you via notification");
                notification.setCreatedDate(LocalDateTime.now());
                notification.setReadStatus("unread"); // Trạng thái chưa đọc

                // Lưu thông báo vào cơ sở dữ liệu và gửi SSE cho client
                notification.addAccount(this_user);
                this_user.addNotification(notification);
                notificationService.saveNotification(notification);
                notificationService.sendNotification(notification); // Gửi SSE tới client
            } else {
                redirectAttributes.addFlashAttribute("error", "Please make sure to transfer the deposit and register fee before the auction registration deadline");
                // Tạo thông báo sau khi đăng ký thành công
                Notification notification = new Notification();
                notification.setContent("Please make sure to transfer the deposit and register fee before the auction registration deadline");
                notification.setCreatedDate(LocalDateTime.now());
                notification.setReadStatus("unread"); // Trạng thái chưa đọc

                // Lưu thông báo vào cơ sở dữ liệu và gửi SSE cho client
                notification.addAccount(this_user);
                this_user.addNotification(notification);
                notificationService.saveNotification(notification);
                notificationService.sendNotification(notification); // Gửi SSE tới client
            }
        }
        return "redirect:/customer/viewAuctionDetail?auctionId=" + auctionId;
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            Principal principal) {

        String username = principal.getName();

        try {
            accountService.changePassword(username, oldPassword, newPassword);
            return ResponseEntity.ok("Password changed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

}

