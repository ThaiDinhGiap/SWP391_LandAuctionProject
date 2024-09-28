package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.AssetDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.entity.LocalAuthority;
import com.se1858.group4.Land_Auction_SWP391.entity.Tag;
import com.se1858.group4.Land_Auction_SWP391.service.AssetService;
import com.se1858.group4.Land_Auction_SWP391.service.LocalAuthorityService;
import com.se1858.group4.Land_Auction_SWP391.service.TagService;
import com.se1858.group4.Land_Auction_SWP391.utility.FileUploadUtil;
import com.se1858.group4.Land_Auction_SWP391.utility.GetSrcInGoogleMapEmbededURL;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/asset")
public class AssetController {
    private AssetService assetService;
    private FileUploadUtil uploadFile;
    private TagService tagService;
    private LocalAuthorityService localAuthorityService;
    public AssetController(AssetService assetService, TagService tagService, FileUploadUtil uploadFile, LocalAuthorityService localAuthorityService) {
        this.assetService = assetService;
        this.uploadFile=uploadFile;
        this.tagService=tagService;
        this.localAuthorityService=localAuthorityService;
    }

    @GetMapping("/register_form")
    public String openAssetRegisterForm(Model model) {
        model.addAttribute("assetDTO",new AssetDTO());
        model.addAttribute("listTag",tagService.getAllTag());
        model.addAttribute("listLocalAuthority",localAuthorityService.getAllLocalAuthority());
        return "asset/AssetRegisterForm";
    }

    @PostMapping("/register")
    public String registerAsset(@ModelAttribute("assetDTO") AssetDTO assetDTO,
                                @RequestParam("images") List<MultipartFile> images,
                                @RequestParam("documents") List<MultipartFile> documents,
                                @RequestParam("selectedTags") List<Integer> selectedTags,
                                @RequestParam(value = "authorityId", required = false) Integer authorityId, //khong bat buoc phai nhan du lieu cua authorityId
                                RedirectAttributes redirectAttributes) {
        Asset asset=assetDTO.getAsset();
        //them image va document
        uploadFile.UploadImages(images,asset);
        uploadFile.UploadDocuments(documents,asset);
        //them tag vao asset
        for (Integer tagId : selectedTags) {
            Optional<Tag> tag = tagService.getTagById(tagId);
            tag.get().addAsset(asset);
            asset.addTag(tag.get());
        }
        //them local authority
        if(authorityId != null){
            Optional<LocalAuthority> localAuthority=localAuthorityService.getLocalAuthorityById(authorityId);
            asset.setLocalAuthority(localAuthority.get());
        }
        else asset.setLocalAuthority(null);
        //luu tai san vao database
        assetService.registerAsset(asset);
        redirectAttributes.addAttribute("message","Property registration successful");
        return "redirect:/asset/register_form"; //Model khong truyen du lieu qua redirect: duoc
    }

    @GetMapping("/list_unsuccessful_sale_asset")
    public String listUnseccessfulSaleAsset(Model model) {
        List<Asset> list=assetService.getAllAssetWithStatus("Not Sold");
        model.addAttribute("listAsset",list);
        return "asset/ListAsset";
    }
    @GetMapping("/get_all_verified_asset")
    public String getAllVerifiedAsset(Model model) {
        List<Asset> list=assetService.getAllAssetWithStatus("Waiting for Auction Scheduling");
        model.addAttribute("listAsset",list);
        return "asset/ListAsset";
    }

    @GetMapping("/viewDetail")
    public String getAssetById(@RequestParam("assetId") int assetId, Model model) {
        Optional<Asset> asset=assetService.getAssetById(assetId);
        String embedUrl = GetSrcInGoogleMapEmbededURL.extractSrcFromIframe(asset.get().getCoordinatesOnMap());
        model.addAttribute("embedUrl", embedUrl);
        model.addAttribute("asset",asset);
        return "asset/AssetDetail";
    }

    @GetMapping("/homepage")
    public String openAssetRegisterForm() {
        return "customer/homepage";
    }
}
