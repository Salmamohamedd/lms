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
    private UserServiceImp userServiceImp;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;

    public List<Notification> getNotificationsForUser(long userId, boolean unreadOnly) {
        System.out.println("Fetching notifications for userId: " + userId + ", unreadOnly: " + unreadOnly);
        List<Notification> notifications;
        if (unreadOnly) {
            notifications = notificationRepository.findByUserIdAndStatus(userId, "UNREAD");
        } else {
            notifications = notificationRepository.findByUserId(userId);
        }
        System.out.println("Fetched notifications: " + notifications);
        return notifications;
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

    public void markAsRead(long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new RuntimeException("Notification not found")
        );
        notification.setStatus("READ");
        notificationRepository.save(notification);
    }

    // Trigger system notification for enrollment confirmation
    public void triggerEnrollmentConfirmation(long studentId, long courseId) {
        Notification notification = new Notification();
        notification.setUserId(studentId);
        notification.setMessage("You have successfully enrolled in the course with ID " + courseId);
        notification.setStatus("UNREAD"); // Set notification as unread
        notificationRepository.save(notification);
    }

    // Trigger system notification for course update
    public void triggerCourseUpdateNotification(long instructorId, long courseId) {
        Notification notification = new Notification();
        notification.setUserId(instructorId);
        notification.setMessage("The course with ID " + courseId + " has been updated.");
        notification.setStatus("UNREAD"); 
        notificationRepository.save(notification);
    }

    // Method to trigger a notification for assignment submission
    public void triggerAssignmentSubmissionNotification(Long studentId, Long assignmentId) {
        // Create a new notification
        Notification notification = new Notification();
        notification.setMessage("Your assignment with ID: " + assignmentId + " has been successfully submitted.");
        notification.setType("ASSIGNMENT_SUBMISSION");
        notification.setId(studentId);  // Student ID who submitted the assignment
        notification.setStatus("UNREAD");

        // Save the notification to the database
        notificationRepository.save(notification);
    }
    // Method to trigger a notification for quiz submission
    public void triggerQuizSubmissionNotification(long studentId, Long quizId) {
        Notification notification = new Notification();
        notification.setId(studentId);
        notification.setMessage("Your quiz with ID: " + quizId + " has been successfully submitted.");
        notification.setType("QUIZ_SUBMISSION");
        notification.setStatus("UNREAD");

        notificationRepository.save(notification);
    }
    // Trigger a notification for graded assignments
    public void triggerGradedAssignmentNotification(Long studentId, Long assignmentId) {
        Notification notification = new Notification();
        notification.setUserId(studentId);
        notification.setMessage("Your assignment with ID: " + assignmentId + " has been graded.");
        notification.setType("GRADED_ASSIGNMENT");
        notification.setStatus("UNREAD");
        notificationRepository.save(notification);

    }
    // Trigger a notification for course-related updates
    public void triggerCourseUpdateNotification(Long studentId, Long courseId, String updateMessage) {
        Notification notification = new Notification();
        notification.setUserId(studentId);
        notification.setMessage("Update for course ID " + courseId + ": " + updateMessage);
        notification.setType("COURSE_UPDATE");
        notification.setStatus("UNREAD");
        notificationRepository.save(notification);
    }
    // Send email notification for course update
    public void sendCourseUpdateEmail(String instructorEmail, long courseId) {
        String subject = "Course Update";
        String body = "The course with ID " + courseId + " has been updated.";
        emailService.sendEmail(instructorEmail, subject, body);
    }
    // Send email notification for enrollment confirmation
    public void sendEnrollmentEmail(String studentEmail, long courseId) {
        String subject = "Enrollment Confirmation";
        String body = "You have successfully enrolled in the course with ID " + courseId;
        emailService.sendEmail(studentEmail, subject, body);
    }
}
