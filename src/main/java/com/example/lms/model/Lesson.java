package com.example.lms.model;
import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ID field
    private String title;
    private String content;
    private String otp; // For attendance
    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    // Constructor
    public Lesson(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Lesson() {

    }

    // Getter and Setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and Setter for content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getter and Setter for otp
    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}