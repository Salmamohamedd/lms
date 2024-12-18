package com.example.lms.controller;


import com.example.lms.model.Submission;
import com.example.lms.service.SubmissionService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private SubmissionService submissionService;


    @PostMapping("/submission/submit")
    @RolesAllowed({"STUDENT"})
    public Submission submit(@RequestBody Submission submission) {
        return submissionService.submit(submission);
    }


    @GetMapping("/submission/{submissionId}")
    @RolesAllowed({"STUDENT"})
    public Submission getSubmissionById(@PathVariable Long submissionId) {
        return submissionService.getSubmissionById(submissionId);
    }

    @DeleteMapping("/submission/delete/{submissionId}")
    @RolesAllowed({"STUDENT"})
    public void deleteSubmission(@PathVariable Long submissionId) {
        submissionService.deleteSubmission(submissionId);
    }





}
