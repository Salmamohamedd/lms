//
//
//package com.example.lms.service;
//
//import com.example.lms.DTO.QuizSubmissionRequest;
//
//import com.example.lms.model.Submission;
//import com.example.lms.repository.SubmissionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class SubmissionService {
//
//    @Autowired
//    private SubmissionRepository submissionRepository;
//    @Autowired
//    private GradesService gradesService;
//
//    // Submit a new submission
//    public Submission submitAssignment(Submission submission) {
//        if (!submission.getType().equalsIgnoreCase("assignment")) {
//            throw new IllegalStateException("This submission is not an assignment.");
//        }
//
//        // Submit a new submission
//        public Submission submit (Submission submission){
//            return submissionRepository.save(submission);
//        }
//
//        // Get a submission by ID
//        public Submission getSubmissionById (Long submissionId){
//            return submissionRepository.findById(submissionId)
//                    .orElseThrow(() -> new RuntimeException("Submission not found"));
//        }
//
//
//        // Delete a submission by ID
//        public void deleteSubmission (Long submissionId){
//            submissionRepository.deleteById(submissionId);
//        }
//
//        public Submission submitQuiz (QuizSubmissionRequest quizSubmissionRequest){
//            Submission submission = gradesService.saveQuizSubmission(quizSubmissionRequest);
//            return gradesService.autoGradeQuiz(submission.getSubmissionId());
//        }
//    }
//}


package com.example.lms.service;
import com.example.lms.DTO.QuizSubmissionRequest;
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
    public Submission submitAssignment(Submission submission) {
        if (!submission.getType().equalsIgnoreCase("assignment")) {
            throw new IllegalStateException("This submission is not an assignment.");
        }

        // Submit a new submission
        public Submission submit (Submission submission){
            return submissionRepository.save(submission);
        }

        // Get a submission by ID
        public Submission getSubmissionById (Long submissionId){
            return submissionRepository.findById(submissionId)
                    .orElseThrow(() -> new RuntimeException("Submission not found"));
        }


        // Delete a submission by ID
        public void deleteSubmission (Long submissionId){
            submissionRepository.deleteById(submissionId);
        }

        public Submission submitQuiz (QuizSubmissionRequest quizSubmissionRequest){
            Submission submission = gradesService.saveQuizSubmission(quizSubmissionRequest);
            return gradesService.autoGradeQuiz(submission.getSubmissionId());
        }

    }
}