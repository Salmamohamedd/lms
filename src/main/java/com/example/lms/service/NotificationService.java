package com.example.lms.service;

import com.example.lms.model.Notification;
import com.example.lms.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // Fetch notifications for a recipient
    public List<Notification> getNotificationsByRecipient(Long recipientId, boolean unreadOnly) {
        if (unreadOnly) {
            return notificationRepository.findByRecipientIdAndReadFalse(recipientId);
        } else {
            return notificationRepository.findByRecipientId(recipientId);
        }
    }

    // Mark a specific notification as read
    public Notification markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    // Mark all notifications for a recipient as read
    public void markAllNotificationsAsRead(Long recipientId) {
        List<Notification> notifications = notificationRepository.findByRecipientIdAndReadFalse(recipientId);
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    // Create a new notification
    public Notification createNotification(Notification notification) {
        notification.setRead(false); // Default to unread
        notification.setTimestamp(java.time.LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    // Delete a notification
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}
