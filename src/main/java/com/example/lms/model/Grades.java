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

    public Grades(){}
    public Grades(Long studentId, Long assessmentId, Double score, String feedback) {
        this.studentId = studentId;
        this.assessmentId = assessmentId;
        this.score = score;
        this.feedback = feedback;
    }

    public Grades(Long gradeId, Long studentId, Long assessmentId, Double score, String feedback) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.assessmentId = assessmentId;
        this.score = score;
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "Grades{" +
                "gradeId=" + gradeId +
                ", studentId=" + studentId +
                ", assessmentId=" + assessmentId +
                ", score=" + score +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
