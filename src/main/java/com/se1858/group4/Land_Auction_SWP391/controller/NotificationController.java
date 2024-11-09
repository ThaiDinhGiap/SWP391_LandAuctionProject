package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.NotificationDTO;
import com.se1858.group4.Land_Auction_SWP391.dto.NotificationRequest;
import com.se1858.group4.Land_Auction_SWP391.dto.SubscriptionRequest;
import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.SubscriptionNotification;
import com.se1858.group4.Land_Auction_SWP391.service.AccountService;
import com.se1858.group4.Land_Auction_SWP391.service.NotificationService;
import com.se1858.group4.Land_Auction_SWP391.service.SubscriptionNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final AccountService accountService;
    private final SubscriptionNotificationService subscriptionNotificationService;

    public NotificationController(NotificationService notificationService, AccountService accountService, SubscriptionNotificationService subscriptionNotificationService) {
        this.notificationService = notificationService;
        this.accountService = accountService;
        this.subscriptionNotificationService = subscriptionNotificationService;
    }

    // Endpoint lấy danh sách thông báo cho một account
    @GetMapping("/get")
    public List<NotificationDTO> getNotifications(@RequestParam("clientId") int clientId) {
        Account account = accountService.findAccountById(clientId);
        return notificationService.getNotificationsForAccount(account);
    }

    // Đánh dấu thông báo là đã đọc
    @PostMapping("/markAsRead")
    public ResponseEntity<?> markAsRead(@RequestParam int notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    // Stream thông báo mới theo thời gian thực bằng SSE
    @GetMapping("/stream/{clientId}")
    public SseEmitter streamNotifications(@PathVariable int clientId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        notificationService.addEmitter(clientId, emitter);

        emitter.onCompletion(() -> notificationService.removeEmitter(clientId));
        emitter.onTimeout(() -> notificationService.removeEmitter(clientId));

        return emitter;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestParam int accountId, @RequestBody SubscriptionRequest subscriptionRequest) {
        System.out.println("Subscription request received for accountId: " + accountId);
        System.out.println("Subscription endpoint: " + subscriptionRequest.getEndpoint());
        // Thêm log để kiểm tra subscriptionRequest
        Account account = accountService.findAccountById(accountId);
        SubscriptionNotification subscription = subscriptionNotificationService.saveSubscription(
                subscriptionRequest.getEndpoint(),
                subscriptionRequest.getP256dh(),
                subscriptionRequest.getAuth(),
                account
        );
        System.out.println("Subscription saved successfully for account: " + accountId);
        return ResponseEntity.ok(subscription);
    }

    // Endpoint hủy đăng ký subscription (unsubscribe)
    @DeleteMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(@RequestParam String endpoint) {
        subscriptionNotificationService.deleteSubscriptionByEndpoint(endpoint);
        return ResponseEntity.ok().build();
    }
}
