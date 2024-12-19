package com.example.lms.service;

import com.example.lms.DTO.StudentProgress;
import com.example.lms.model.AssessmentType;
import com.example.lms.model.Attendance;
import com.example.lms.model.Grades;
import com.example.lms.repository.AssessmentRepository;
import com.example.lms.repository.AttendanceRepository;
import com.example.lms.repository.GradesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProgressService {
    @Autowired
    private GradesRepository gradesRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;

    public StudentProgress getStudentProgress(Long studentId, Long courseId){
        //get student's grades at the specified course
        List<Grades> gradesList = gradesRepository.findGradesByStudentIdAndCourseId(studentId, courseId);

        // Separate quiz and assignment grades
        List<Grades> quizGrades = new ArrayList<>();
        List<Grades> assignmentGrades = new ArrayList<>();

        for (Grades grade: gradesList) {
            Optional<AssessmentType> optionalType = assessmentRepository.findTypeByAssessmentId(grade.getAssessmentId());
            if (optionalType.isPresent()) {
                AssessmentType type = optionalType.get();
                if (type == AssessmentType.QUIZ) {
                    quizGrades.add(grade);
                } else if (type == AssessmentType.ASSIGNMENT) {
                    assignmentGrades.add(grade);
                }
            }
        }

        Integer totalQuizzes = quizGrades.size();
        Integer totalAssignments = assignmentGrades.size();
        //get average quizzes scores
        Double averageQuizScore = quizGrades.stream().mapToDouble(Grades::getScore).average().orElse(0.0);
        //get average assignments scores
        Double averageAssignmentScore = assignmentGrades.stream().mapToDouble(Grades::getScore).average().orElse(0.0);

        // Fetch attendance
//        Attendance attendance = attendanceRepository.findAttendanceByStudentIdAndCourseId(studentId, courseId)
//                .orElseThrow(() -> new IllegalStateException("Attendance data not found for student ID " + studentId + " and course ID " + courseId));
//        //get attendance percentage
//        Double attendancePercentage = (double)attendance.getLessonsAttended() / attendance.getTotalLessons() * 100;

        return new StudentProgress(totalQuizzes, totalAssignments, averageQuizScore, averageAssignmentScore);
    }
}
