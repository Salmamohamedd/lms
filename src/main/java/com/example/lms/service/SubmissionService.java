package com.example.lms.service;

import com.example.lms.model.Submission;
import com.example.lms.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private GradesService gradesService;

    // Submit a new submission
    public Submission submit(Submission submission) {
        Submission savedSubmission = submissionRepository.save(submission);
        if (savedSubmission.getType().equalsIgnoreCase("quiz")){
            Submission gradedQuiz = gradesService.autoGradeQuiz(savedSubmission.getSubmissionId());
            return gradedQuiz;
        }
        return savedSubmission;
    }

    // Get a submission by ID
    public Submission getSubmissionById(Long submissionId) {
        return submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
    }


    // Delete a submission by ID
    public void deleteSubmission(Long submissionId) {
        submissionRepository.deleteById(submissionId);
    }
}
