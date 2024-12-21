package com.example.lms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String recipientType; // "STUDENT" or "INSTRUCTOR"

    @Column(nullable = false)
    private Long recipientId; // User ID

    @Column(nullable = false)
    private boolean read; // Flag to track if the notification is read

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Default constructor
    public Notification() {
    }

    // Parameterized constructor
    public Notification(String message, String recipientType, Long recipientId, boolean read, LocalDateTime timestamp) {
        this.message = message;
        this.recipientType = recipientType;
        this.recipientId = recipientId;
        this.read = read;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Override equals and hashCode for entity comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return read == that.read &&
                Objects.equals(id, that.id) &&
                Objects.equals(message, that.message) &&
                Objects.equals(recipientType, that.recipientType) &&
                Objects.equals(recipientId, that.recipientId) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, recipientType, recipientId, read, timestamp);
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", recipientType='" + recipientType + '\'' +
                ", recipientId=" + recipientId +
                ", read=" + read +
                ", timestamp=" + timestamp +
                '}';
    }
}
