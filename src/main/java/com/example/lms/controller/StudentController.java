

package com.example.lms.controller;

import com.example.lms.DTO.QuizSubmissionRequest;
import com.example.lms.config.JwtService;
import com.example.lms.model.*;
import com.example.lms.service.CourseService;
import com.example.lms.service.GradesService;
import com.example.lms.service.SubmissionService;
import com.example.lms.service.UserServiceImp;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.example.lms.model.Course;
import com.example.lms.model.Lesson;
import com.example.lms.service.CourseService;
import com.example.lms.service.NotificationService;
import com.example.lms.model.Submission;
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
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private GradesService gradesService;

    @Autowired
    private NotificationService notificationService;
    //Grading related endpoint
    @GetMapping("/allGrades/{studentId}")
    @RolesAllowed({"STUDENT"})
    public List<Grades> getStudentGrades(@PathVariable Long studentId){
        return gradesService.viewStudentGrades(studentId);
    }
    @GetMapping("/allGrades")
    @RolesAllowed({"STUDENT"})
    public List<Grades> getStudentCourseGrades(@RequestParam Long studentId, @RequestParam Long courseId){
        return gradesService.viewStudentCourseGrades(studentId, courseId);
    }

    @GetMapping("/quizGrade")
    @RolesAllowed({"STUDENT"})
    public Grades getQuizGrade(@RequestParam Long studentId, @RequestParam Long quizId){
        return gradesService.getQuizGrade(studentId, quizId);
    }
    @GetMapping("/answers")
    @RolesAllowed({"STUDENT"})
    public List<StudentAnswers> getStudentAnswers(@RequestParam Long studentId, @RequestParam Long submissionId){
        return gradesService.getstudentAnswers(studentId, submissionId);
    }
    ///////////////////
    //Assessment related endpoints
    @PostMapping("/submission/submit-assignment")
    @RolesAllowed({"STUDENT"})
    public Submission submitAssignment(@RequestBody Submission submission) {
        return submissionService.submitAssignment(submission);
    }

    @PostMapping("/submission/submit-quiz")
    @RolesAllowed({"STUDENT"})
    public Submission submitQuiz(@RequestBody QuizSubmissionRequest quizSubmissionRequest){
        return submissionService.submitQuiz(quizSubmissionRequest);
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


    /////////////////////////////////

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
    public boolean verifyOtp(@PathVariable Long courseId, @PathVariable Long lessonId, @RequestParam Long studentId, @RequestParam String otp) {
        return courseService.verifyOtp(courseId, lessonId, studentId, otp);
    }

    ////////////////////////////////
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
    // Fetch system notifications for a student
    @GetMapping("/notifications")
    @RolesAllowed({"STUDENT"})
    public ResponseEntity<List<Notification>> getNotifications(@RequestHeader("Authorization") String authorizationHeader,
                                                               @RequestParam(required = false, defaultValue = "false") boolean unreadOnly) {
        String token = authorizationHeader.substring(7); // Remove "Bearer "
        String email = jwtService.extractClaim(token, "sub"); // Extract email from token

        User student = userServiceImp.getUserByEmail(email);
        List<Notification> notifications = notificationService.getNotificationsForUser(student.getId(), unreadOnly);

        return ResponseEntity.ok(notifications);
    }

    // Update notification preferences
    @PutMapping("/notifications/preferences")
    @RolesAllowed({"STUDENT"})
    public ResponseEntity<String> updateNotificationPreferences(@RequestHeader("Authorization") String authorizationHeader,
                                                                @RequestBody NotificationPreferences preferences) {
        String token = authorizationHeader.substring(7);
        String email = jwtService.extractClaim(token, "sub");

        User student = userServiceImp.getUserByEmail(email);
        notificationService.updateNotificationPreferences(student.getId(), preferences);

        return ResponseEntity.ok("Notification preferences updated successfully.");
    }

    // Mark a notification as read
    @PutMapping("/notifications/{id}/read")
    @RolesAllowed({"STUDENT"})
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok("Notification marked as read.");
    }
    @PostMapping("/courses/{courseId}/enroll")
    @RolesAllowed({"STUDENT"})
    public ResponseEntity<String> enrollInCourse(@PathVariable int courseId, @RequestHeader("Authorization") String authorizationHeader) {
        // Enroll student in course
        String token = authorizationHeader.substring(7);
        String email = jwtService.extractClaim(token, "sub");
        User student = userServiceImp.getUserByEmail(email);

        // Trigger system notification
        notificationService.triggerEnrollmentConfirmation(student.getId(), courseId);

        // Send email notification
        notificationService.sendEnrollmentEmail(student.getEmail(), courseId);

        return ResponseEntity.ok("Student enrolled successfully!");
    }

}