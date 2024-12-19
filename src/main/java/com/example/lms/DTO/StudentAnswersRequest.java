package com.example.lms.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentAnswersRequest {

    private Long questionId;
    private String answer;
}
