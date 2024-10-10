package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.Staff;
import com.se1858.group4.Land_Auction_SWP391.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final StaffService staffService;

    @Autowired
    public ChatController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping("/connect/{staffId}")
    public ResponseEntity<Map<String, String>> connectClientToStaff(@PathVariable Integer staffId, @RequestParam Integer clientId) {
        Map<String, String> response = new HashMap<>();
        boolean staffAvailable = staffService.isStaffAvailable(staffId);

        if (staffAvailable) {
            staffService.setStaffAvailability(staffId, false);
            Staff staff = staffService.findById(staffId);
            staffService.requestStaffConfirmation(staff.getAccount().getAccountId(), clientId);
            response.put("status", "Pending");
        } else {
            response.put("status", "Rejected");
        }

        return ResponseEntity.ok(response);
    }
}
