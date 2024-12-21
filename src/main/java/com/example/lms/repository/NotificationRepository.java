package com.example.lms.repository;

import com.example.lms.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Find all notifications for a recipient
    List<Notification> findByRecipientId(Long recipientId);

    // Find unread notifications for a recipient
    List<Notification> findByRecipientIdAndReadFalse(Long recipientId);
}
