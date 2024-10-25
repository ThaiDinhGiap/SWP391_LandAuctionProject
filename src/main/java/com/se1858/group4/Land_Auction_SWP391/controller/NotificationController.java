package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.NotificationDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.service.AccountService;
import com.se1858.group4.Land_Auction_SWP391.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final AccountService accountService;

    public NotificationController(NotificationService notificationService, AccountService accountService) {
        this.notificationService = notificationService;
        this.accountService = accountService;
    }

    @GetMapping("/get")
    public List<NotificationDTO> getNotifications(@RequestParam("clientId") int clientId) {
        // Tìm Account tương ứng với clientId
        Account account = accountService.findAccountById(clientId);

        // Trả về danh sách các thông báo của account
        return notificationService.getNotificationsForAccount(account);
    }

    @PostMapping("/markAsRead")
    public ResponseEntity<?> markAsRead(@RequestParam int notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/stream/{clientId}")
    public SseEmitter streamNotifications(@PathVariable int clientId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // Đặt timeout vô hạn (tùy chỉnh thời gian nếu cần)

        // Đăng ký SSE cho client với clientId
        notificationService.addEmitter(clientId, emitter);

        emitter.onCompletion(() -> notificationService.removeEmitter(clientId));
        emitter.onTimeout(() -> notificationService.removeEmitter(clientId));

        return emitter;
    }
}
