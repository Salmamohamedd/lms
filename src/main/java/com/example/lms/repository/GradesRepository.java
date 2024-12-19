package com.example.lms.repository;

import com.example.lms.model.Grades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradesRepository extends JpaRepository<Grades, Long> {
    List<Grades> findGradesByStudentId(Long studentId);
    Grades findGradeByStudentIdAndAssessmentId(Long studentId, Long quizId);
    List<Grades> findGradesByStudentIdAndCourseId(Long studentId, Long courseId);
}
