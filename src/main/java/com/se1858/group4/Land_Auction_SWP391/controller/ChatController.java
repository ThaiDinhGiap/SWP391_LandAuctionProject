package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.Staff;
import com.se1858.group4.Land_Auction_SWP391.service.FirebaseChatService;
import com.se1858.group4.Land_Auction_SWP391.service.StaffService;
import com.se1858.group4.Land_Auction_SWP391.websocket.ConnectMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final StaffService staffService;
    private FirebaseChatService firebaseChatService;

    @Autowired
    public ChatController(StaffService staffService, FirebaseChatService firebaseChatService) {
        this.staffService = staffService;
        this.firebaseChatService = firebaseChatService;
    }

    @PostMapping("/connect/{staffId}")
    public ResponseEntity<Map<String, String>> connectClientToStaff(@PathVariable Integer staffId, @RequestParam Integer clientId) {
        Map<String, String> response = new HashMap<>();
        boolean staffAvailable = staffService.isStaffAvailable(staffId);

        if (staffAvailable) {
            Staff staff = staffService.findById(staffId);
            staffService.setStaffAvailability(staff.getAccount().getAccountId(), false);

            ConnectMessage requestMessage = new ConnectMessage();
            requestMessage.setClientId(clientId);
            requestMessage.setStaffId(staff.getAccount().getAccountId());
            requestMessage.setStatus("Pending");

            staffService.requestStaffConfirmation(requestMessage);
            response.put("status", "Pending");
        } else {
            response.put("status", "Rejected");
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/messages/{sessionId}")
    public List<Map<String, Object>> getChatMessages(@PathVariable String sessionId) throws InterruptedException {
        return firebaseChatService.getChatMessages(sessionId);
    }

    @GetMapping("/history/{staffId}")
    public List<Map<String, Object>> getAllChatHistoryByStaffId(@PathVariable Integer staffId) throws InterruptedException {
        return firebaseChatService.getChatSessionsByStaffId(staffId);
    }

}
