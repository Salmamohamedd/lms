package com.example.lms.repository;

import com.example.lms.model.Submission;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SubmissionRepository {
    private final List<Submission> submissions = new ArrayList<>();
    private Long submissionIdCounter = 1L;

    // Get all submissions
    public List<Submission> findAll() {
        return submissions;
    }

    // Find a submission by ID
    public Optional<Submission> findById(Long id) {
        return submissions.stream().filter(submission -> submission.getSubmissionId().equals(id)).findFirst();
    }

    // Save a new or existing submission
    public Submission save(Submission submission) {
        if (submission.getSubmissionId() == null) {
            submission.setSubmissionId(submissionIdCounter++);
        }
        submissions.add(submission);
        return submission;
    }

    // Delete a submission by ID
    public void deleteById(Long id) {
        submissions.removeIf(submission -> submission.getSubmissionId().equals(id));
    }
}