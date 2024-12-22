package com.example.lms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    private Long studentId;
    private Long courseId;
    private int totalLessons;
    private int lessonsAttended;

    public Attendance(Long studentId, Long courseId, int totalLessons, int lessonsAttended) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.totalLessons = totalLessons;
        this.lessonsAttended = lessonsAttended;
    }
}
