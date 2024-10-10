package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.BanLog;
import com.se1858.group4.Land_Auction_SWP391.service.BanLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Controller
@RequestMapping("/banLog")
public class BanLogController {
    BanLogService banLogService;

    public BanLogController(){
    }

    public BanLogController(BanLogService banLogService) {
        this.banLogService = banLogService;
    }

    @GetMapping("/list")
    public String listBanLog(Model theModel) {

        // get the employees from db
        List<BanLog> banLogs = banLogService.findAll();
        // add to the spring model
        theModel.addAttribute("banLogs", banLogs);
        return "account/list-ban-log";
    }
}
