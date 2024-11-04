package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.StaffDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.LocalAuthority;
import com.se1858.group4.Land_Auction_SWP391.service.LocalAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.se1858.group4.Land_Auction_SWP391.dto.StaffDTO;
import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import com.se1858.group4.Land_Auction_SWP391.utility.FileUploadUtil;

@Controller
//@RequestMapping("/LocalAuthority")
public class LocalAuthorityController {

    @Autowired
    private LocalAuthorityService localAuthorityService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private FileUploadUtil uploadFile;

    @GetMapping("/LocalAuthority")
    public String getListOfLocalAuthority(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "localAuthorityName") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String keyword,
            Model model) {

        Page<LocalAuthority> localAuthorityPage = localAuthorityService.findAllWithPaginationAndSorting(page, size, sortField, sortDir, keyword);

        model.addAttribute("localAuthorityList", localAuthorityPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", localAuthorityPage.getTotalPages());
        model.addAttribute("totalItems", localAuthorityPage.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);

        return "account/dashboard";
    }

    @GetMapping("/AddLocalAuthority")
    public String showAddLocalAuthorityForm(Model model) {
        model.addAttribute("authority", new LocalAuthority());
        return "account/icons";
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
        return "account/map"; // Tên tệp HTML của trang cập nhật
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

    @GetMapping("/local-profile")
    public String profile(Model model) {
        Account account = userDetailsService.accountAuthenticated();
        if (account != null) {
            StaffDTO staffDTO = new StaffDTO();
            staffDTO.setAccount(account);
            staffDTO.setStaff(account.getStaff());
            model.addAttribute("staffDTO", staffDTO);
        }
        return "account/profile";
    }
    @PostMapping("/uploadAvatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile avatar, Model model) {
        Account account = userDetailsService.accountAuthenticated();
        if (account != null&&avatar!=null) {
            uploadFile.UploadAvatar(avatar, account);
        }
        return "redirect:/local-profile";
    }
    @GetMapping("icon")
    public String adminHomes() {
        return "dashboardTemplates/icons";
    }

    @GetMapping("/DeleteLocalAuthority/{id}")
    public String deleteLocalAuthority(@PathVariable("id") Integer id) {
        localAuthorityService.deleteLocalAuthority(id);
        return "redirect:/LocalAuthority";
    }
}

