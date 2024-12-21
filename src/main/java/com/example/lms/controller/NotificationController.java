package com.example.lms.controller;

import com.example.lms.model.Notification;
import com.example.lms.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Fetch all notifications for a recipient (all or unread only)
    @GetMapping("/{recipientId}")
    public ResponseEntity<List<Notification>> getNotifications(
            @PathVariable Long recipientId,
            @RequestParam(defaultValue = "false") boolean unreadOnly) {
        List<Notification> notifications = notificationService.getNotificationsByRecipient(recipientId, unreadOnly);
        return ResponseEntity.ok(notifications);
    }

    // Mark a notification as read
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long notificationId) {
        Notification updatedNotification = notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok(updatedNotification);
    }

    // Mark all notifications for a recipient as read
    @PutMapping("/{recipientId}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long recipientId) {
        notificationService.markAllNotificationsAsRead(recipientId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Add a new notification
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification createdNotification = notificationService.createNotification(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNotification);
    }

    // Delete a notification by ID
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
