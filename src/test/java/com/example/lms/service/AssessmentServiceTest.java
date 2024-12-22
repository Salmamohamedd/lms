package com.example.lms.service;

import com.example.lms.model.Assessment;
import com.example.lms.repository.AssessmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssessmentServiceTest {

    @InjectMocks
    private AssessmentService assessmentService;

    @Mock
    private AssessmentRepository assessmentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAssessment() {
        Assessment assessment = new Assessment();
        assessment.setTitle("Test Assessment");

        when(assessmentRepository.save(any(Assessment.class))).thenReturn(assessment);

        Assessment savedAssessment = assessmentService.createAssessment(assessment);

        assertNotNull(savedAssessment);
        assertEquals("Test Assessment", savedAssessment.getTitle());
        verify(assessmentRepository, times(1)).save(assessment);
    }

    @Test
    void getAllAssessments() {
        Assessment assessment1 = new Assessment();
        assessment1.setTitle("Assessment 1");
        Assessment assessment2 = new Assessment();
        assessment2.setTitle("Assessment 2");

        List<Assessment> assessments = Arrays.asList(assessment1, assessment2);

        when(assessmentRepository.findAll()).thenReturn(assessments);

        List<Assessment> result = assessmentService.getAllAssessments();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Assessment 1", result.get(0).getTitle());
        assertEquals("Assessment 2", result.get(1).getTitle());
        verify(assessmentRepository, times(1)).findAll();
    }

    @Test
    void getAssessmentById() {
        Assessment assessment = new Assessment();
        assessment.setTitle("Test Assessment");

        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assessment));

        Assessment result = assessmentService.getAssessmentById(1L);

        assertNotNull(result);
        assertEquals("Test Assessment", result.getTitle());
        verify(assessmentRepository, times(1)).findById(1L);
    }

    @Test
    void updateAssessment() {
        Assessment existingAssessment = new Assessment();
        existingAssessment.setTitle("Old Title");

        Assessment updatedAssessment = new Assessment();
        updatedAssessment.setTitle("New Title");

        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(existingAssessment));
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(existingAssessment);

        Assessment result = assessmentService.updateAssessment(1L, updatedAssessment);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        verify(assessmentRepository, times(1)).findById(1L);
        verify(assessmentRepository, times(1)).save(existingAssessment);
    }

    @Test
    void deleteAssessment() {
        doNothing().when(assessmentRepository).deleteById(1L);

        assessmentService.deleteAssessment(1L);

        verify(assessmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAssessmentsByCourse() {
        Assessment assessment1 = new Assessment();
        assessment1.setTitle("Assessment 1");
        Assessment assessment2 = new Assessment();
        assessment2.setTitle("Assessment 2");

        List<Assessment> assessments = Arrays.asList(assessment1, assessment2);

        when(assessmentRepository.findByCourseId(1L)).thenReturn(assessments);

        List<Assessment> result = assessmentService.getAssessmentsByCourse(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Assessment 1", result.get(0).getTitle());
        assertEquals("Assessment 2", result.get(1).getTitle());
        verify(assessmentRepository, times(1)).findByCourseId(1L);
    }
}
