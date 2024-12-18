package com.example.lms.controller;

import com.example.lms.model.Course;
import com.example.lms.model.Lesson;
import com.example.lms.service.CourseService;
import com.example.lms.model.Submission;
import com.example.lms.service.SubmissionService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private CourseService courseService;


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


    // New course-related endpoints

    @GetMapping("/getCourses")
    @RolesAllowed({"STUDENT"})
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PostMapping("/courses/{id}/courseEnroll")
    @RolesAllowed({"STUDENT"})
    public void enrollInCourse(@PathVariable Long id, @RequestParam String studentName) {
        courseService.enrollStudent(id, studentName);
    }

    @GetMapping("/courses/{id}/getLessons")
    @RolesAllowed({"STUDENT"})
    public List<Lesson> getLessons(@PathVariable Long id) {
        return courseService.getLessonsForCourse(id);
    }

    @PostMapping("/courses/{courseId}/lessons/{lessonId}/verifyOtp")
    @RolesAllowed({"STUDENT"})
    public boolean verifyOtp(@PathVariable Long courseId, @PathVariable Long lessonId, @RequestParam String otp) {
        return courseService.verifyOtp(courseId, lessonId, otp);
    }




}
