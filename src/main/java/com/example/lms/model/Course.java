package com.example.lms.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import java.util.ArrayList;

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;
    private String title;
    private String description;
    private String duration;
    @ElementCollection
    private List<String> mediaFiles = new ArrayList<>();
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();
    @ElementCollection
    private List<String> enrolledStudents = new ArrayList<>();


    // Getter and Setter for id
    public Long getId() {
        return courseId;
    }

    public void setId(Long id) {
        this.courseId = id;
    }

}


