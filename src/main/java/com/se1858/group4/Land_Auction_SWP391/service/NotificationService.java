package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.dto.NotificationDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.Notification;
import com.se1858.group4.Land_Auction_SWP391.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // Chuyển đổi Notification sang NotificationDTO
    private NotificationDTO convertToDTO(Notification notification) {
        return new NotificationDTO(notification.getNotificationId(),
                notification.getContent(),
                notification.getCreatedDate(),
                notification.getReadStatus());
    }

    public NotificationDTO saveNotification(Notification notification) {
        Notification savedNotification = notificationRepository.save(notification);
        return convertToDTO(savedNotification);
    }

    public List<NotificationDTO> getNotificationsForAccount(Account account) {
        List<Notification> notifications = notificationRepository.findByAccountIdOrderByCreatedDateDesc(account.getAccountId());
        return notifications.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void markNotificationAsRead(int notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setReadStatus("read");
        notificationRepository.save(notification); // Cập nhật trạng thái trong database
    }

    // Thêm emitter mới cho client
    public void addEmitter(int clientId, SseEmitter emitter) {
        emitters.put(clientId, emitter);
    }

    // Xóa emitter khi client ngắt kết nối
    public void removeEmitter(int clientId) {
        emitters.remove(clientId);
    }

    // Gửi thông báo mới cho tất cả các client liên quan
    public void sendNotification(Notification notification) {
        // Chuyển đổi đối tượng Notification sang DTO
        NotificationDTO notificationDTO = convertToDTO(notification);

        notification.getAccounts().forEach(account -> {
            SseEmitter emitter = emitters.get(account.getAccountId());
            if (emitter != null) {
                try {
                    // Gửi NotificationDTO thay vì đối tượng Notification
                    emitter.send(SseEmitter.event().name("newNotification").data(notificationDTO));
                } catch (IOException e) {
                    emitters.remove(account.getAccountId());
                }
            }
        });
    }

}
