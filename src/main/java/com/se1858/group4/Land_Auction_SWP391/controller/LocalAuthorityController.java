package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.LocalAuthority;
import com.se1858.group4.Land_Auction_SWP391.service.LocalAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/api/local-authorities")
@Controller
public class LocalAuthorityController {

    @Autowired
    private LocalAuthorityService localAuthorityService;

    @GetMapping
    public List<LocalAuthority> getAllLocalAuthorities() {
        return localAuthorityService.findAll();
    }

    @GetMapping("/LocalAuthority")
    public String getListOfLocalAuthority(Model model) {
        List<LocalAuthority> localAuthorityList = localAuthorityService.findAll();
        model.addAttribute("localAuthorityList", localAuthorityList);
        return "localAuthorityDashboard/dashboard";
    }

    @GetMapping("/AddLocalAuthority")
    public String showAddLocalAuthorityForm(Model model) {
        model.addAttribute("authority", new LocalAuthority());
        return "localAuthorityDashboard/icons";
    }
    @PostMapping("/AddLocalAuthority")
    public String addLocalAuthority(@ModelAttribute("authority") LocalAuthority authority) {
        localAuthorityService.saveAuthority(authority);
        return "redirect:/LocalAuthority";
    }

    @GetMapping("/UpdateLocalAuthority/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        LocalAuthority localAuthority = localAuthorityService.findById(id); // Sửa thành Local_authority
        model.addAttribute("localAuthority", localAuthority);
        return "localAuthorityDashboard/map"; // Tên tệp HTML của trang cập nhật
    }

    @PostMapping("/UpdateLocalAuthority")
    public String updateLocalAuthority(@ModelAttribute LocalAuthority localAuthority) {
        // Tìm authority theo ID và cập nhật các trường
        LocalAuthority existingAuthority = localAuthorityService.findById(localAuthority.getLocalAuthorityId());

        // Cập nhật các trường
        existingAuthority.setLocalAuthorityName(localAuthority.getLocalAuthorityName());
        existingAuthority.setContactPerson(localAuthority.getContactPerson());
        existingAuthority.setPhoneNumber(localAuthority.getPhoneNumber());
        existingAuthority.setEmail(localAuthority.getEmail());
        existingAuthority.setLocalAuthorityAddress(localAuthority.getLocalAuthorityAddress());

        localAuthorityService.saveAuthority(existingAuthority); // Lưu bản cập nhật
        return "redirect:/LocalAuthority"; // Chuyển hướng về trang dashboard
    }
}
