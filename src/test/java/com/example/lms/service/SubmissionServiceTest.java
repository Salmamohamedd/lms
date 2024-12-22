package com.example.lms.service;

import com.example.lms.DTO.QuizSubmissionRequest;
import com.example.lms.model.Submission;
import com.example.lms.repository.SubmissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubmissionServiceTest {

    @InjectMocks
    private SubmissionService submissionService;

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private GradesService gradesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void submitAssignment() {
        Submission assignmentSubmission = new Submission();
        assignmentSubmission.setType("assignment");

        when(submissionRepository.save(any(Submission.class))).thenReturn(assignmentSubmission);

        Submission savedSubmission = submissionService.submitAssignment(assignmentSubmission);

        assertNotNull(savedSubmission);
        assertEquals("assignment", savedSubmission.getType());
        verify(submissionRepository, times(1)).save(assignmentSubmission);
    }

    @Test
    void submitAssignment_shouldThrowExceptionForInvalidType() {
        Submission invalidSubmission = new Submission();
        invalidSubmission.setType("quiz");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            submissionService.submitAssignment(invalidSubmission);
        });

        assertEquals("This submission is not an assignment.", exception.getMessage());
        verify(submissionRepository, never()).save(invalidSubmission);
    }

    @Test
    void getSubmissionById() {
        Submission submission = new Submission();
        submission.setSubmissionId(1L);

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(submission));

        Submission result = submissionService.getSubmissionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getSubmissionId());
        verify(submissionRepository, times(1)).findById(1L);
    }

    @Test
    void getSubmissionById_shouldThrowExceptionIfNotFound() {
        when(submissionRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            submissionService.getSubmissionById(1L);
        });

        assertEquals("Submission not found", exception.getMessage());
        verify(submissionRepository, times(1)).findById(1L);
    }

    @Test
    void deleteSubmission() {
        doNothing().when(submissionRepository).deleteById(1L);

        submissionService.deleteSubmission(1L);

        verify(submissionRepository, times(1)).deleteById(1L);
    }

    @Test
    void submitQuiz() {
        QuizSubmissionRequest quizSubmissionRequest = new QuizSubmissionRequest();
        Submission quizSubmission = new Submission();
        quizSubmission.setSubmissionId(1L);

        when(gradesService.saveQuizSubmission(quizSubmissionRequest)).thenReturn(quizSubmission);
        when(gradesService.autoGradeQuiz(1L)).thenReturn(quizSubmission);

        Submission result = submissionService.submitQuiz(quizSubmissionRequest);

        assertNotNull(result);
        assertEquals(1L, result.getSubmissionId());
        verify(gradesService, times(1)).saveQuizSubmission(quizSubmissionRequest);
        verify(gradesService, times(1)).autoGradeQuiz(1L);
    }
}
