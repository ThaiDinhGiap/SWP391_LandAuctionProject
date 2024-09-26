package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.AssetDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.entity.Image;
import com.se1858.group4.Land_Auction_SWP391.repository.ImageRepository;
import com.se1858.group4.Land_Auction_SWP391.service.AssetService;
import com.se1858.group4.Land_Auction_SWP391.utility.UploadFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/asset")
public class AssetController {
    private AssetService assetService;
    private ImageRepository imageRepository;
    private UploadFile uploadFile;
    public AssetController(AssetService assetService, ImageRepository imageRepository, UploadFile uploadFile) {
        this.assetService = assetService;
        this.imageRepository=imageRepository;
        this.uploadFile=uploadFile;
    }

    @GetMapping("/register_form")
    public String openAssetRegisterForm(Model model) {
        model.addAttribute("assetDTO",new AssetDTO());
        return "AssetRegisterForm";
    }

    @PostMapping("/register")
    public String registerAsset(@ModelAttribute("assetDTO") AssetDTO assetDTO,
                                @RequestParam("images") List<MultipartFile> images,
                                @RequestParam("documents") List<MultipartFile> documents,
                                RedirectAttributes redirectAttributes) {
        Asset asset=assetDTO.getAsset();
        uploadFile.UploadImages(images,asset);
        uploadFile.UploadDocuments(documents,asset);
        assetService.registerAsset(asset);
        redirectAttributes.addAttribute("message","Dang ky tai san thanh cong");
        return "redirect:/asset/register_form"; //Model khong truyen du lieu qua redirect: duoc
    }

    @GetMapping("/list_unsuccessful_sale_asset")
    public String listUnseccessfulSaleAsset(Model model) {
        List<Asset> list=assetService.getAllAssetWithStatus("Ban khong thanh cong");
        model.addAttribute("listAsset",list);
        return "ListAsset";
    }
    @GetMapping("/get_all_verified_asset")
    public String getAllVerifiedAsset(Model model) {
        List<Asset> list=assetService.getAllAssetWithStatus("Dang xac minh");
        model.addAttribute("listAsset",list);
        return "ListAsset";
    }

    @GetMapping("/viewDetail")
    public String getAssetById(@RequestParam("assetId") int assetId, Model model) {
        Optional<Asset> asset=assetService.getAssetById(assetId);
        model.addAttribute("asset",asset);
        return "AssetDetail";
    }

    @GetMapping("/homepage")
    public String openAssetRegisterForm() {
        return "customer/homepage";
    }
}
