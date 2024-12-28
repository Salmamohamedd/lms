package com.example.lms.repository;

import com.example.lms.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long studentId);
    List<Notification> findByUserIdAndStatus(Long studentId, String status);
}
