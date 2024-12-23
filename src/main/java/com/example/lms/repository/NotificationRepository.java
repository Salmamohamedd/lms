package com.example.lms.repository;

import com.example.lms.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(int userId);
    List<Notification> findByUserIdAndIsRead(int userId, boolean isRead);
}
