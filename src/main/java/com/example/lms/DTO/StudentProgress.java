package com.example.lms.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StudentProgress {
    private Integer attendedQuizzes;
    private Integer submittedAssignments;
    private Double averageQuizScore;
    private Double averageAssignmentScore;
//    private Double attendancePercentage;
}
