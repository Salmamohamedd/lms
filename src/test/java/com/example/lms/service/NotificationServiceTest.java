package com.example.lms.service;

import com.example.lms.model.Notification;
import com.example.lms.model.NotificationPreferences;
import com.example.lms.model.User;
import com.example.lms.repository.NotificationRepository;
import com.example.lms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserServiceImp userServiceImp;

    private Notification notification;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test objects
        notification = new Notification();
        notification.setId(1L);
        notification.setUserId(100L);
        notification.setMessage("Test Notification");
        notification.setStatus("UNREAD");

        user = new User();
        user.setId(100);
        user.setEmail("test@example.com");
        user.setEmailNotifications(true);
    }

    @Test
    void testGetNotificationsForUser_UnreadOnly() {
        // Mock behavior
        List<Notification> notifications = List.of(notification);
        when(notificationRepository.findByUserIdAndStatus(100L, "UNREAD")).thenReturn(notifications);

        // Test method
        List<Notification> result = notificationService.getNotificationsForUser(100L, true);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Notification", result.get(0).getMessage());
    }

    @Test
    void testGetNotificationsForUser_All() {
        // Mock behavior
        List<Notification> notifications = List.of(notification);
        when(notificationRepository.findByUserId(100L)).thenReturn(notifications);

        // Test method
        List<Notification> result = notificationService.getNotificationsForUser(100L, false);

        // Assertions
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Notification", result.get(0).getMessage());
    }

    @Test
    void testUpdateNotificationPreferences() {
        // Mock behavior
        NotificationPreferences preferences = new NotificationPreferences();
        preferences.setEmailNotifications(true);
        preferences.setStudentEnrollments(false);
        preferences.setCourseUpdates(true);
        when(userRepository.findById(Math.toIntExact(100L))).thenReturn(Optional.of(user));

        // Test method
        notificationService.updateNotificationPreferences(100, preferences);

        // Verify
        verify(userRepository, times(1)).save(user);
        assertTrue(user.isEmailNotifications());
        assertFalse(user.isStudentEnrollments());
        assertTrue(user.isCourseUpdates());
    }

    @Test
    void testMarkAsRead() {
        // Mock behavior
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        // Test method
        notificationService.markAsRead(1L);

        // Verify
        assertEquals("READ", notification.getStatus());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void testTriggerEnrollmentConfirmation() {
        // Test method
        notificationService.triggerEnrollmentConfirmation(100L, 200L);

        // Verify
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testTriggerCourseUpdateNotification() {
        // Test method
        notificationService.triggerCourseUpdateNotification(100L, 200L);

        // Verify
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testTriggerAssignmentSubmissionNotification() {
        // Test method
        notificationService.triggerAssignmentSubmissionNotification(100L, 300L);

        // Verify
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testTriggerQuizSubmissionNotification() {
        // Test method
        notificationService.triggerQuizSubmissionNotification(100L, 400L);

        // Verify
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testTriggerGradedAssignmentNotification() {
        // Test method
        notificationService.triggerGradedAssignmentNotification(100L, 500L);

        // Verify
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testSendCourseUpdateEmail() {
        // Test method
        notificationService.sendCourseUpdateEmail("instructor@example.com", 600L);

        // Verify
        verify(emailService, times(1)).sendEmail(
                eq("instructor@example.com"),
                eq("Course Update"),
                contains("600")
        );
    }

    @Test
    void testSendEnrollmentEmail() {
        // Test method
        notificationService.sendEnrollmentEmail("student@example.com", 700L);

        // Verify
        verify(emailService, times(1)).sendEmail(
                eq("student@example.com"),
                eq("Enrollment Confirmation"),
                contains("700")
        );
    }
}
