package com.example.lms.service;

import com.example.lms.DTO.StudentProgress;
import com.example.lms.model.AssessmentType;
import com.example.lms.model.Grades;
import com.example.lms.repository.AssessmentRepository;
import com.example.lms.repository.AttendanceRepository;
import com.example.lms.repository.GradesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProgressServiceTest {
    @InjectMocks
    private ProgressService progressService;
    @Mock
    private GradesRepository gradesRepository;
    @Mock
    private AssessmentRepository assessmentRepository;
    @Mock
    private AttendanceService attendanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStudentProgress_shouldReturnCorrectProgress() {
        Long studentId = 1L, courseId = 101L;
        Grades grade1 = new Grades(studentId, 1L, 80.0,"Excellent", courseId); // Quiz grade
        Grades grade2 = new Grades(studentId,2L,90.0,"Excellent", courseId); // Assignment grade
        Grades grade3 = new Grades(studentId, 3L,85.0,"Well done",courseId); // Quiz grade

        List<Grades> gradesList = List.of(grade1, grade2, grade3);
        when(gradesRepository.findGradesByStudentIdAndCourseId(studentId, courseId)).thenReturn(gradesList);

        //Mock AssessmentRepository to return AssessmentType for each grade
        when(assessmentRepository.findTypeByAssessmentId(1L)).thenReturn(Optional.of(AssessmentType.QUIZ));
        when(assessmentRepository.findTypeByAssessmentId(2L)).thenReturn(Optional.of(AssessmentType.ASSIGNMENT));
        //Mock AttendanceService to return a dummy attendance percentage
        when(attendanceService.getAttendancePercentage(studentId, courseId)).thenReturn(95.0);

        StudentProgress studentProgress = progressService.getStudentProgress(studentId, courseId);

        assertNotNull(studentProgress, "Student progress should not be null");
        // Total quizzes should be 2 (grade1 and grade3)
        assertEquals(1, studentProgress.getAttendedQuizzes(), "Total quizzes do not match");
        // Total assignments should be 1 (grade2)
        assertEquals(1, studentProgress.getSubmittedAssignments(), "Total assignments do not match");
        // Average quiz score should be (80 + 85) / 2 = 82.5
        assertEquals(80.0, studentProgress.getAverageQuizScore(), 0.01, "Average quiz score is incorrect");
        // Average assignment score should be 90.0 (only one assignment grade)
        assertEquals(90.0, studentProgress.getAverageAssignmentScore(), 0.01, "Average assignment score is incorrect");
        // Attendance percentage should be 95.0
        assertEquals(95.0, studentProgress.getAttendancePercentage(), 0.01, "Attendance percentage is incorrect");
        // Verify that the necessary repository methods were called
        verify(gradesRepository, times(1)).findGradesByStudentIdAndCourseId(studentId, courseId);
        verify(assessmentRepository, times(3)).findTypeByAssessmentId(anyLong()); // Two grades to check
        verify(attendanceService, times(1)).getAttendancePercentage(studentId, courseId);
    }
}