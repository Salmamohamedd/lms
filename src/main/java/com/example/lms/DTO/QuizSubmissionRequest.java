package com.example.lms.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class QuizSubmissionRequest {
    private Long quizId;
    private  Long studentId;
    private List<StudentAnswersRequest> studentAnswers;
}
