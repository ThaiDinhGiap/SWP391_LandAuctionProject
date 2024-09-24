package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.service.AssetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/assets")
public class AssetController {
    private AssetService assetService;
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }
    @GetMapping("/asset_register_form")
    public String openAssetRegisterForm(Model model) {
        model.addAttribute("asset",new Asset());
        return "AssetRegisterForm";
    }
    @GetMapping("/list_unsuccessful_sale_asset")
    public String listUnseccessfulSaleAsset(Model model) {
        List<Asset> list=assetService.getAllUnseccessfulSaleAsset();
        model.addAttribute("listUnseccessfulSaleAsset",list);
        return "ListAsset";
    }
    @PostMapping("/registerAsset")
    public String registerAsset(@ModelAttribute("asset") Asset asset, Model model) {
        assetService.registerAsset(asset);
        model.addAttribute("message","Dang ky tai san thanh cong");
        return "redirect:/assets/asset_register_form";
    }
}
