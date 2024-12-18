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

    // Submit a new submission
    public Submission submit(Submission submission) {
        return submissionRepository.save(submission);
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
