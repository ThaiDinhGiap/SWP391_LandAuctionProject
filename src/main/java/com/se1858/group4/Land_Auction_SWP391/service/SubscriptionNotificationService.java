package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.dto.NotificationDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.SubscriptionNotification;
import com.se1858.group4.Land_Auction_SWP391.repository.SubscriptionNotificationRepository;
import org.springframework.stereotype.Service;

import nl.martijndwars.webpush.PushService;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import java.nio.charset.StandardCharsets;
import nl.martijndwars.webpush.Notification;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionNotificationService {

    private final SubscriptionNotificationRepository subscriptionNotificationRepository;
    private PushService pushService;

    public SubscriptionNotificationService(SubscriptionNotificationRepository subscriptionNotificationRepository, PushService pushService) {
        this.subscriptionNotificationRepository = subscriptionNotificationRepository;
        this.pushService = pushService;
    }

    // Lưu một subscription mới
    public SubscriptionNotification saveSubscription(String endpoint, String p256dh, String auth, Account account) {
        SubscriptionNotification subscription = new SubscriptionNotification(endpoint, p256dh, auth, account);
        return subscriptionNotificationRepository.save(subscription);
    }

    // Lấy tất cả các subscription của một account
    public List<SubscriptionNotification> getSubscriptionsForAccount(int accountId) {
        return subscriptionNotificationRepository.findByAccountAccountId(accountId);
    }

    // Xóa subscription theo endpoint
    public void deleteSubscriptionByEndpoint(String endpoint) {
        subscriptionNotificationRepository.deleteByEndpoint(endpoint);
    }

    // Tìm subscription theo endpoint
    public Optional<SubscriptionNotification> getSubscriptionByEndpoint(String endpoint) {
        return subscriptionNotificationRepository.findByEndpoint(endpoint);
    }

//    // Gửi thông báo đến tất cả các subscriptions của một account
//    public void sendNotificationToAccount(int accountId, String title, String body) {
//        List<SubscriptionNotification> subscriptions = getSubscriptionsForAccount(accountId);
//        for (SubscriptionNotification subscription : subscriptions) {
//            // Hàm này sẽ sử dụng Web Push để gửi thông báo
//            sendWebPushNotification(subscription, title, body);
//        }
//    }

    // Hàm để gửi thông báo web push (Web Push Notification)
    public void sendWebPushNotification(NotificationDTO notificationDTO, int accountId) {
        // Lấy danh sách SubscriptionNotification cho account
        List<SubscriptionNotification> subscriptions = subscriptionNotificationRepository.findByAccountAccountId(accountId);
        subscriptions.forEach(subscription -> {
            try {
                // Tạo payload cho Web Push Notification
                JsonObject notificationPayload = Json.object()
                        .add("title", "New notification from VietLand Auction")
                        .add("message", notificationDTO.getContent());

                // Tạo Notification cho Web Push (không phải entity Notification của bạn)
                Notification notification = new Notification(
                        subscription.getEndpoint(),
                        subscription.getP256dh(),
                        subscription.getAuth(),
                        notificationPayload.toString().getBytes(StandardCharsets.UTF_8)
                );

                // Gửi thông báo qua pushService
                pushService.send(notification);
            } catch (Exception e) {
                e.printStackTrace(); // Xử lý lỗi khi gửi thông báo
            }
        });
    }
}
