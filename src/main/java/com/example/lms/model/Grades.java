package com.example.lms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Setter
@Getter
public class Grades {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gradeId;

    private Long studentId;
    private Long assessmentId;
    private Double score;
    private String feedback;
    private Long courseId;

    public Grades(){}
    public Grades(Long studentId, Long assessmentId, Double score, String feedback, Long courseId) {
        this.studentId = studentId;
        this.assessmentId = assessmentId;
        this.score = score;
        this.feedback = feedback;
        this.courseId = courseId;
    }

    public Grades(Long gradeId, Long studentId, Long assessmentId, Double score, String feedback, Long courseId) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.assessmentId = assessmentId;
        this.score = score;
        this.feedback = feedback;
        this.courseId = courseId;
    }

}
