package com.example.lms.repository;

import com.example.lms.model.StudentAnswers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAnswersRepository extends JpaRepository<StudentAnswers, Long> {

    List<StudentAnswers> findStudentAnswersBySubmissionId(Long submissionId);

    List<StudentAnswers> findStudentAnswersByStudentIdAndSubmissionId(Long studentId, Long submissionId);
}
