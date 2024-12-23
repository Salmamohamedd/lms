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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Notification notification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);
        user.setEmail("test@example.com");

        notification = new Notification();
        notification.setId(1);
        notification.setUserId(1);
        notification.setMessage("Test Notification");
        notification.setRead(false);
    }

    @Test
    void testGetNotificationsForUser_UnreadOnly() {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(notification);
        when(notificationRepository.findByUserIdAndIsRead(1, false)).thenReturn(notifications);

        List<Notification> result = notificationService.getNotificationsForUser(1, true);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Notification", result.get(0).getMessage());
        verify(notificationRepository, times(1)).findByUserIdAndIsRead(1, false);
    }

    @Test
    void testGetNotificationsForUser_All() {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(notification);
        when(notificationRepository.findByUserId(1)).thenReturn(notifications);

        List<Notification> result = notificationService.getNotificationsForUser(1, false);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(notificationRepository, times(1)).findByUserId(1);
    }

    @Test
    void testUpdateNotificationPreferences() {
        NotificationPreferences preferences = new NotificationPreferences();
        preferences.setEmailNotifications(true);
        preferences.setStudentEnrollments(true);
        preferences.setCourseUpdates(false);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        notificationService.updateNotificationPreferences(1, preferences);

        assertTrue(user.isEmailNotifications());
        assertTrue(user.isStudentEnrollments());
        assertFalse(user.isCourseUpdates());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testMarkAsRead() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        notificationService.markAsRead(1L);

        assertTrue(notification.isRead());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void testTriggerEnrollmentConfirmation() {
        notificationService.triggerEnrollmentConfirmation(1, 101);

        verify(notificationRepository, times(1)).save(argThat(n ->
                n.getUserId() == 1 &&
                        n.getMessage().equals("You have successfully enrolled in the course with ID 101") &&
                        !n.isRead()
        ));
    }

    @Test
    void testTriggerCourseUpdateNotification() {
        notificationService.triggerCourseUpdateNotification(1, 101L);

        verify(notificationRepository, times(1)).save(argThat(n ->
                n.getUserId() == 1 &&
                        n.getMessage().equals("The course with ID 101 has been updated.") &&
                        !n.isRead()
        ));
    }

    @Test
    void testSendEnrollmentEmail() {
        notificationService.sendEnrollmentEmail("student@example.com", 101);

        verify(emailService, times(1)).sendEmail(
                eq("student@example.com"),
                eq("Enrollment Confirmation"),
                eq("You have successfully enrolled in the course with ID 101")
        );
    }

    @Test
    void testSendCourseUpdateEmail() {
        notificationService.sendCourseUpdateEmail("instructor@example.com", 101L);

        verify(emailService, times(1)).sendEmail(
                eq("instructor@example.com"),
                eq("Course Update"),
                eq("The course with ID 101 has been updated.")
        );
    }
}