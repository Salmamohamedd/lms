package com.example.lms.model;

public class NotificationPreferences {
    private boolean emailNotifications;
    private boolean studentEnrollments;
    private boolean courseUpdates;

    // Getters and Setters
    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public boolean isStudentEnrollments() {
        return studentEnrollments;
    }

    public void setStudentEnrollments(boolean studentEnrollments) {
        this.studentEnrollments = studentEnrollments;
    }

    public boolean isCourseUpdates() {
        return courseUpdates;
    }

    public void setCourseUpdates(boolean courseUpdates) {
        this.courseUpdates = courseUpdates;
    }
}
