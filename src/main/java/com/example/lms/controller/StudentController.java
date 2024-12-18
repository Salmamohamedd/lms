package com.example.lms.controller;

import com.example.lms.config.JwtService;
import com.example.lms.model.Course;
import com.example.lms.model.Grades;
import com.example.lms.model.Lesson;
import com.example.lms.model.User;
import com.example.lms.service.CourseService;
import com.example.lms.model.Submission;
import com.example.lms.service.GradesService;
import com.example.lms.service.SubmissionService;
import com.example.lms.service.UserServiceImp;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private CourseService courseService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private GradesService gradesService;

    //Grading related endpoint
    @GetMapping("/allGrades/{studentId}")
    @RolesAllowed({"STUDENT"})
    public List<Grades> getStudentGrades(@PathVariable Long studentId){
        return gradesService.viewStudentGrades(studentId);
    }

    @GetMapping("/quizGrade")
    @RolesAllowed({"STUDENT"})
    public Grades getQuizGrade(@RequestParam Long studentId, @RequestParam Long quizId){
        return gradesService.getQuizGrade(studentId, quizId);
    }

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

    // profile-related endpoints
    @GetMapping("/viewStudentProfile")
    @RolesAllowed({"STUDENT"})
    public ResponseEntity<?> getById(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the user ID from the token
        String token = authorizationHeader.substring(7); // Remove "Bearer "
        String email = jwtService.extractClaim(token, "sub"); // "sub" claim in token contains the email

        // Fetch the user by ID (if needed)
        User student = userServiceImp.getUserByEmail(email);

        // Return the user with HTTP status 200 OK
        return ResponseEntity.ok(student);
    }

    @PutMapping("/updateStudentProfile")
    @RolesAllowed({"STUDENT"})
    public ResponseEntity<?> updateById(@RequestHeader("Authorization") String authorizationHeader,
                                        @RequestBody User user){
        String token = authorizationHeader.substring(7);
        String email = jwtService.extractClaim(token, "sub");

        // Fetch the user by ID (if needed)
        User student = userServiceImp.getUserByEmail(email);
        userServiceImp.updateById(student.getId(), user);
        user.setId(student.getId());
        return ResponseEntity.ok(user);
    }



}
