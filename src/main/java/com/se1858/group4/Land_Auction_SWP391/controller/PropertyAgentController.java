package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.AssetDTO;
import com.se1858.group4.Land_Auction_SWP391.dto.StaffDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.*;
import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import com.se1858.group4.Land_Auction_SWP391.service.*;
import com.se1858.group4.Land_Auction_SWP391.utility.FileUploadUtil;
import com.se1858.group4.Land_Auction_SWP391.utility.GetSrcInGoogleMapEmbededURLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/property_agent")
public class PropertyAgentController {
    private AssetService assetService;
    private FileUploadUtil uploadFile;
    private TagService tagService;
    private LocalAuthorityService localAuthorityService;
    private AccountService accountService;
    private TaskService taskService;
    private UserDetailsService userDetailsService;
    public PropertyAgentController(AssetService assetService, FileUploadUtil uploadFile, TagService tagService,
                                   LocalAuthorityService localAuthorityService, AccountService accountService,
                                   TaskService taskService, UserDetailsService userDetailsService) {
        this.assetService = assetService;
        this.uploadFile = uploadFile;
        this.tagService = tagService;
        this.localAuthorityService = localAuthorityService;
        this.accountService = accountService;
        this.taskService = taskService;
        this.userDetailsService = userDetailsService;
    }
    @GetMapping("/dashboard")
    public String dashboard() {
        return "propertyAgent/Dashboard";
    }
    @GetMapping("/detail")
    public String detail() {
        return "propertyAgent/AssetDetail";
    }
    @GetMapping("/register_form")
    public String openAssetRegisterForm(Model model) {
        model.addAttribute("assetDTO",new AssetDTO());
        model.addAttribute("listTag",tagService.getAllTag());
        model.addAttribute("listLocalAuthority",localAuthorityService.getAllLocalAuthority());
        model.addAttribute("listAuctioneer",accountService.findAllStaffsByRole("ROLE_Auctioneer"));
        return "propertyAgent/AssetRegisterForm";
    }

    @PostMapping("/register")
    public String registerAsset(@ModelAttribute("assetDTO") AssetDTO assetDTO,
                                @RequestParam("images") List<MultipartFile> images,
                                @RequestParam("documents") List<MultipartFile> documents,
                                @RequestParam("selectedTags") List<Integer> selectedTags,
                                @RequestParam(value = "authorityId", required = false) Integer authorityId, //khong bat buoc phai nhan du lieu cua authorityId
                                @RequestParam(value = "auctioneerAccountId", required = true) Integer auctioneerAccountId,
                                RedirectAttributes redirectAttributes) {
        Asset asset=assetDTO.getAsset();
        //them image va document
        uploadFile.UploadImagesForAsset(images,asset);
        uploadFile.UploadDocumentsForAsset(documents,asset);
        //them tag vao asset
        for (Integer tagId : selectedTags) {
            Tag tag = tagService.getTagById(tagId);
            tag.addAsset(asset);
            asset.addTag(tag);
        }
        //them local authority
        if(authorityId != null){
            LocalAuthority localAuthority=localAuthorityService.getLocalAuthorityById(authorityId);
            asset.setLocalAuthority(localAuthority);
        }
        else asset.setLocalAuthority(null);
        //luu tai san vao database
        Account this_property_agent=userDetailsService.accountAuthenticated();
        assetService.registerAsset(asset);
        Task task=new Task();
        task.setPropertyAgent(this_property_agent); //mac dinh la bun bo hue
        task.setAuctioneer(accountService.findAccountById(auctioneerAccountId));
        task.setAsset(asset);
        taskService.save(task);
        redirectAttributes.addAttribute("message","Property registration successful");
        return "redirect:/property_agent/register_form"; //Model khong truyen du lieu qua redirect: duoc
    }
    @GetMapping("/get_all_verified_asset")
    public String getAllVerifiedAsset(Model model) {
        List<Asset> list=assetService.getAllAssetWithStatus("Waiting for Auction Scheduling");
        model.addAttribute("listAsset",list);
        model.addAttribute("pageTitle","All verified assets");
        model.addAttribute("deletePermission","false");
        return "propertyAgent/AssetList";
    }
    @GetMapping("/list_unsuccessful_sale_asset")
    public String listUnseccessfulSaleAsset(Model model) {
        List<Asset> list=assetService.getAllAssetWithStatus("Failed sale");
        model.addAttribute("listAsset",list);
        model.addAttribute("pageTitle","Unsuccessful sale assets");
        model.addAttribute("deletePermission","true");
        return "propertyAgent/AssetList";
    }
    @GetMapping("/get_all_asset")
    public String listAllAsset(Model model) {
        List<Asset> list=assetService.getAllAsset();
        model.addAttribute("listAsset",list);
        model.addAttribute("pageTitle","Asset list");
        model.addAttribute("deletePermission","false");
        return "propertyAgent/AssetList";
    }
    @GetMapping("/cancelAsset")
    public String cancelAsset(@RequestParam("assetId") int assetId) {
        assetService.cancelAssetById(assetId);
        return "redirect:/property_agent/list_unsuccessful_sale_asset";
    }
    @GetMapping("/viewDetail")
    public String getAssetById(@RequestParam("assetId") int assetId, Model model) {
        Asset asset=assetService.getAssetById(assetId);
        String embedUrl = GetSrcInGoogleMapEmbededURLUtil.extractSrcFromIframe(asset.getCoordinatesOnMap());
        model.addAttribute("embedUrl", embedUrl);
        model.addAttribute("asset",asset);
        return "propertyAgent/AssetDetail";
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
        return "propertyAgent/profile";
    }

    @PostMapping("/uploadAvatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile avatar, Model model) {
        Account account = userDetailsService.accountAuthenticated();
        if (account != null&&avatar!=null) {
            uploadFile.UploadAvatar(avatar, account);
        }
        return "redirect:/property_agent/profile";
    }
}
