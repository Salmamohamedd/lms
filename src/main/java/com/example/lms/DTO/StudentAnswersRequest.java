package com.example.lms.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StudentAnswersRequest {

    private Long questionId;
    private String answer;
}
