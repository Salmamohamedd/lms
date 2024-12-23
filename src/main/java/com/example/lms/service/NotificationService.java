package com.example.lms.service;

import com.example.lms.model.Notification;
import com.example.lms.model.User;
import com.example.lms.repository.NotificationRepository;
import com.example.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.lms.model.NotificationPreferences;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    public List<Notification> getNotificationsForUser(int userId, boolean unreadOnly) {
        if (unreadOnly) {
            return notificationRepository.findByUserIdAndIsRead(userId, false);
        }
        return notificationRepository.findByUserId(userId);
    }

    public void updateNotificationPreferences(int userId, NotificationPreferences preferences) {

        // Fetch the user entity by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));

        // Update the user's notification preferences
        user.setEmailNotifications(preferences.isEmailNotifications());
        user.setStudentEnrollments(preferences.isStudentEnrollments());
        user.setCourseUpdates(preferences.isCourseUpdates());

        // Save the updated user entity
        userRepository.save(user);
    }


    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new RuntimeException("Notification not found")
        );
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    // Trigger system notification for enrollment confirmation
    public void triggerEnrollmentConfirmation(int studentId, int courseId) {
        Notification notification = new Notification();
        notification.setUserId(studentId);
        notification.setMessage("You have successfully enrolled in the course with ID " + courseId);
        notification.setRead(false); // Set notification as unread
        notificationRepository.save(notification);
    }

    // Trigger system notification for course update
    public void triggerCourseUpdateNotification(int instructorId, Long courseId) {
        Notification notification = new Notification();
        notification.setUserId(instructorId);
        notification.setMessage("The course with ID " + courseId + " has been updated.");
        notification.setRead(false); // Set notification as unread
        notificationRepository.save(notification);
    }

    // Send email notification for enrollment confirmation
    public void sendEnrollmentEmail(String studentEmail, int courseId) {
        String subject = "Enrollment Confirmation";
        String body = "You have successfully enrolled in the course with ID " + courseId;
        emailService.sendEmail(studentEmail, subject, body);
    }

    // Send email notification for course update
    public void sendCourseUpdateEmail(String instructorEmail, Long courseId) {
        String subject = "Course Update";
        String body = "The course with ID " + courseId + " has been updated.";
        emailService.sendEmail(instructorEmail, subject, body);
    }

}
