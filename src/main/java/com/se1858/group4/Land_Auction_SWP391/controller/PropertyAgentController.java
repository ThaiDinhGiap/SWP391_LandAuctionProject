package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.AssetDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.entity.LocalAuthority;
import com.se1858.group4.Land_Auction_SWP391.entity.Tag;
import com.se1858.group4.Land_Auction_SWP391.entity.Task;
import com.se1858.group4.Land_Auction_SWP391.service.*;
import com.se1858.group4.Land_Auction_SWP391.utility.FileUploadUtil;
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
    public PropertyAgentController(AssetService assetService, FileUploadUtil uploadFile, TagService tagService,
                                   LocalAuthorityService localAuthorityService, AccountService accountService,
                                   TaskService taskService) {
        this.assetService = assetService;
        this.uploadFile = uploadFile;
        this.tagService = tagService;
        this.localAuthorityService = localAuthorityService;
        this.accountService = accountService;
        this.taskService = taskService;
    }
    @GetMapping("/dashboard")
    public String dashboard() {
        return "propertyAgent/Dashboard";
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
        uploadFile.UploadImages(images,asset);
        uploadFile.UploadDocuments(documents,asset);
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
        assetService.registerAsset(asset);
        Task task=new Task();
        task.setPropertyAgent(accountService.findAccountById(3)); //mac dinh la bun bo hue
        task.setAuctioneer(accountService.findAccountById(auctioneerAccountId));
        task.setAsset(asset);
        taskService.save(task);
        redirectAttributes.addAttribute("message","Property registration successful");
        return "redirect:/property_agent/register_form"; //Model khong truyen du lieu qua redirect: duoc
    }
    @GetMapping("/list_unsuccessful_sale_asset")
    public String listUnseccessfulSaleAsset(Model model) {
        List<Asset> list=assetService.getAllAssetWithStatus("Failed sale");
        model.addAttribute("listAsset",list);
        return "propertyAgent/UnsuccessfulSaleAssets";
    }
    @GetMapping("/cancelAsset")
    public String cancelAsset(@RequestParam("assetId") int assetId) {
        assetService.cancelAssetById(assetId);
        return "redirect:/property_agent/list_unsuccessful_sale_asset";
    }
}
