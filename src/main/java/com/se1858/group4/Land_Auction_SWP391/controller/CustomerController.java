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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    private ImageService imageService;
    private FileUploadUtil uploadFile;
    private CustomerService customerService;
    private QrCode qrCode;


    public CustomerController(NewsService newsService, TagForNewsService tagForNewsService,
                              AssetService assetService, TagService tagService, AuctionService auctionService,
                              UserDetailsService userDetailsService, AccountService accountService,
                              ImageService imageService, FileUploadUtil uploadFile,
                              CustomerService customerService, QrCode qrCode) {
        this.newsService = newsService;
        this.tagForNewsService = tagForNewsService;
        this.assetService = assetService;
        this.tagService = tagService;
        this.auctionService = auctionService;
        this.userDetailsService = userDetailsService;
        this.accountService = accountService;
        this.imageService = imageService;
        this.uploadFile = uploadFile;
        this.customerService = customerService;
        this.qrCode = qrCode;
    }


    @GetMapping("/get_all_asset")
    public String getAllAsset(Model model) {
        List<Asset> newsList = assetService.getAllAssetWithStatus("Waiting for Auction Scheduling");
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
            Model model) {


        List<AuctionSession> filteredAuctions = auctionService.filterAuctionSessions(keyword, fromDate, toDate);
        model.addAttribute("listAuction", filteredAuctions);
        return "customer/auctionList :: auctionListFragment";
    }


    @GetMapping("/viewNewsDetail")
    public String getNewsById(@RequestParam("newsId") int newsId, Model model) {
        News news = newsService.getNewsById(newsId);
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
        Asset asset = assetService.getAssetById(assetId);
        String embedUrl = GetSrcInGoogleMapEmbededURLUtil.extractSrcFromIframe(asset.getCoordinatesOnMap());
        model.addAttribute("embedUrl", embedUrl);
        model.addAttribute("asset", asset);
        return "customer/assetDetail";
    }


    @GetMapping("/viewAuctionDetail")
    public String getAuctionById(@RequestParam("auctionId") int auctionId, Model model) {
        AuctionSession auction = auctionService.getAuctionSessionById(auctionId);
        String embedUrl = GetSrcInGoogleMapEmbededURLUtil.extractSrcFromIframe(auction.getAsset().getCoordinatesOnMap());
        model.addAttribute("embedUrl", embedUrl);
        model.addAttribute("auction", auction);
        return "customer/auctionDetail";
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


    @PostMapping("/registerAuction")
    public String registerAuction(@RequestParam("validate") String validate,
                                  @RequestParam("auctionId") int auctionId, Model model) {


        AuctionSession auction = auctionService.getAuctionSessionById(auctionId);
        String embedUrl = GetSrcInGoogleMapEmbededURLUtil.extractSrcFromIframe(auction.getAsset().getCoordinatesOnMap());
        model.addAttribute("embedUrl", embedUrl);
        model.addAttribute("auction", auction);
        //check xem nguoi dung da validate tai khoan chua
        //kiem tra xem nguoi dung da tick het chua
        if (validate != null) {
            //tao ma QR chuyen tien coc
            qrCode.setAmount(auction.getDeposit() + auction.getRegisterFee() + "");
            qrCode.setDescription("User id 10 " + "transfer deposit, fee");
            model.addAttribute("qrCode", qrCode);
        }
        return "customer/auctionDetail";
    }


    @PostMapping("/transferDepositAndFee")
    public String transferDepositAndFee() {


        return "customer/auctionDetail";
    }


}


