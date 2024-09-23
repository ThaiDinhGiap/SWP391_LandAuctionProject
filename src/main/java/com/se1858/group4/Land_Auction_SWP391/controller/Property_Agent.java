package com.se1858.group4.Land_Auction_SWP391.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Property_Agent {
	@GetMapping("/asset_register_form")
	public String openAssetRegisterForm() {
		return "AssetRegisterForm";
	}
}
