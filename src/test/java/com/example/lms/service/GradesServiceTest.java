package com.example.lms.service;

import com.example.lms.DTO.QuizSubmissionRequest;
import com.example.lms.DTO.StudentAnswersRequest;
import com.example.lms.model.*;
import com.example.lms.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GradesServiceTest {
    @Mock
    private SubmissionRepository submissionRepository;
    @Mock
    private GradesRepository gradesRepository;
    @Mock
    private AssessmentRepository assessmentRepository;
    @InjectMocks
    private GradesService gradesService;
    @Mock
    private StudentAnswersRepository studentAnswersRepository;
    @Mock
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void gradeAssignment_shouldSaveGradeAndSubmission() {
        Long submissionId = 1L;
        Double score = 90.0;
        String feedback = "Well done";

        Submission submission = new Submission();
        submission.setSubmissionId(submissionId);
        submission.setType("assignment");
        submission.setAssessmentId(2L);
        submission.setStudentId(3L);

        Assessment assessment = new Assessment();
        assessment.setCourseId(4L);

        when(submissionRepository.findById(submissionId)).thenReturn(Optional.of(submission));
        when(assessmentRepository.findById(submission.getAssessmentId())).thenReturn(Optional.of(assessment));
        when(submissionRepository.save(any(Submission.class))).thenReturn(submission);

        Submission result = gradesService.gradeAssignment(submissionId, score, feedback);

        assertEquals(score, result.getScore());
        assertEquals(feedback, result.getFeedback());
        verify(gradesRepository, times(1)).save(any(Grades.class));
        verify(submissionRepository, times(1)).save(submission);
    }

    @Test
    void autoGradeQuiz_shouldCalculateScoreAndSaveSubmission() {
        Long submissionId = 1L;

        Submission submission = new Submission();
        submission.setSubmissionId(submissionId);
        submission.setType("quiz");
        submission.setAssessmentId(2L);
        submission.setStudentId(3L);

        Question question1 = new Question();
        Question question2 = new Question();
        question1.setQuestionId(1L);
        question1.setQuestionText("Q1");
        question1.setCorrectAnswer("A");
        question1.setAssessmentId(2L);
        question2.setQuestionId(2L);
        question2.setQuestionText("Q2");
        question2.setCorrectAnswer("B");
        question2.setAssessmentId(2L);

        StudentAnswers studentAnswers1 = new StudentAnswers(1L,3L, submissionId, 1L,"A");
        StudentAnswers studentAnswers2 = new StudentAnswers(2L,3L, submissionId, 1L,"D");

        Assessment assessment = new Assessment();
        assessment.setCourseId(4L);

        when(submissionRepository.findById(submissionId)).thenReturn(Optional.of(submission));
        when(studentAnswersRepository.findStudentAnswersBySubmissionId(submissionId)).thenReturn(List.of(studentAnswers1, studentAnswers2));
        when(questionRepository.findAllByAssessmentId(submission.getAssessmentId())).thenReturn(List.of(question1, question2));
        when(assessmentRepository.findById(submission.getAssessmentId())).thenReturn(Optional.of(assessment));
        when(submissionRepository.save(any(Submission.class))).thenReturn(submission);

        Submission result = gradesService.autoGradeQuiz(submissionId);
        assertNotNull(result);
        assertEquals(50.0, result.getScore());
        assertEquals("Good", result.getFeedback());
        verify(gradesRepository, times(1)).save(any(Grades.class));
        verify(submissionRepository, times(1)).save(submission);
    }

    @Test
    void saveQuizSubmission_shouldSaveSubmissionAndAnswers() {
        QuizSubmissionRequest quizSubmissionRequest = new QuizSubmissionRequest();
        quizSubmissionRequest.setQuizId(1L);
        quizSubmissionRequest.setStudentId(2L);
        quizSubmissionRequest.setStudentAnswers(List.of(
                new StudentAnswersRequest(3L, "Answer1"),
                new StudentAnswersRequest(4L, "Answer2")
        ));

        Submission savedSubmission = new Submission();
        savedSubmission.setSubmissionId(5L); // Set the expected submissionId

        // Mock the submissionRepository.save() to return the savedSubmission object with submissionId 5L
        when(submissionRepository.save(any(Submission.class))).thenAnswer(invocation -> {
            Submission submission = invocation.getArgument(0);
            submission.setSubmissionId(5L); // Ensure submissionId is set correctly before returning
            return submission;
        });
        // Mock the studentAnswersRepository.save() to return the saved student answers
        StudentAnswers savedAnswer1 = new StudentAnswers();
        savedAnswer1.setSubmissionId(5L);
        savedAnswer1.setStudentId(2L);
        savedAnswer1.setQuestionId(3L);
        savedAnswer1.setGivenAnswer("Answer1");

        StudentAnswers savedAnswer2 = new StudentAnswers();
        savedAnswer2.setSubmissionId(5L);
        savedAnswer2.setStudentId(2L);
        savedAnswer2.setQuestionId(4L);
        savedAnswer2.setGivenAnswer("Answer2");

        // Mock the save behavior for both answers
        when(studentAnswersRepository.save(any(StudentAnswers.class)))
                .thenReturn(savedAnswer1)
                .thenReturn(savedAnswer2);

        Submission result = gradesService.saveQuizSubmission(quizSubmissionRequest);

        assertNotNull(result, "Resulting submission should not be null");
        assertEquals(5L, result.getSubmissionId(), "SubmissionId does not match");
        verify(submissionRepository, times(1)).save(any(Submission.class)); // Ensure save was called once for submission
        verify(studentAnswersRepository, times(2)).save(any(StudentAnswers.class)); // Ensure save was called twice for answers
    }


}