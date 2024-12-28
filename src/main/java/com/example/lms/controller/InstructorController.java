package com.example.lms.controller;

import com.example.lms.DTO.StudentProgress;
import com.example.lms.config.JwtService;
import com.example.lms.model.NotificationPreferences;

import com.example.lms.model.*;
import com.example.lms.service.*;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.PublicKey;
import java.util.List;

import com.example.lms.model.Assessment;
import com.example.lms.model.Question;
import com.example.lms.service.AssessmentService;
import com.example.lms.service.CourseService;
import com.example.lms.service.NotificationService;
import com.example.lms.service.QuestionService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.lms.model.Lesson;
import com.example.lms.model.Course;
import com.example.lms.service.CourseService;
@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CourseService courseService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private GradesService gradesService;
    @Autowired
    private ProgressService progressService;
    @Autowired
    private PerformanceReportService performanceReportService;
    @Autowired
    private ChartService chartService;
    @Autowired
    private NotificationService notificationService;
    //Grading related endpoints
    @PostMapping("/gradeAss")
    @RolesAllowed({"INSTRUCTOR"})
    public Submission gradeAssignment(@RequestBody Submission submission){
        Submission gradedSubmission = gradesService.gradeAssignment(submission.getSubmissionId(), submission.getScore(), submission.getFeedback());
        notificationService.triggerGradedAssignmentNotification(gradedSubmission.getStudentId(), submission.getSubmissionId());
        return gradedSubmission;
    }
    ////////////////////////////
    //Performance tracking related endpoints
    @GetMapping("/student-progress")
    @RolesAllowed({"INSTRUCTOR"})
    public StudentProgress getStudentProgress(@RequestParam Long studentId, @RequestParam Long courseId){
        return progressService.getStudentProgress(studentId, courseId);
    }
    //////////////////////////
    //performance analytics related endpoint
    @GetMapping("/report")
    @RolesAllowed({"INSTRUCTOR"})
    public ResponseEntity<byte[]> generateReport(@RequestParam Long courseId) throws IOException, IOException {
        byte[] report = performanceReportService.generatePerformanceReport(courseId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=performance_report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(report);
    }
    @GetMapping("/chart")
    @RolesAllowed({"INSTRUCTOR"})
    public ResponseEntity<byte[]> getPerformanceChart(@RequestParam Long courseId) throws IOException {
        byte[] chart = chartService.generatePerformanceChart(courseId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=performance_chart.png")
                .contentType(MediaType.IMAGE_PNG)
                .body(chart);
    }

    /////////////////////////
    //Assessments related endpoints


    @PostMapping("/assessments/create")
    @RolesAllowed({"INSTRUCTOR"})
    public Assessment createAssessment(@RequestBody Assessment assessment) {
        return assessmentService.createAssessment(assessment);
    }

    @GetMapping("/assessments/all")
    @RolesAllowed({"INSTRUCTOR"})
    public List<Assessment> getAllAssessments() {
        return assessmentService.getAllAssessments();
    }

    @PutMapping("/assessments/update/{assessmentId}")
    @RolesAllowed({"INSTRUCTOR"})
    public Assessment updateAssessment(@PathVariable Long assessmentId, @RequestBody Assessment updatedAssessment) {
        return assessmentService.updateAssessment(assessmentId, updatedAssessment);
    }

    @DeleteMapping("/assessments/delete/{assessmentId}")
    @RolesAllowed({"INSTRUCTOR"})
    public void deleteAssessment(@PathVariable Long assessmentId) {
        assessmentService.deleteAssessment(assessmentId);
    }


    @PostMapping("/questions/add")
    @RolesAllowed({"INSTRUCTOR"})
    public Question addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }

    @GetMapping("/questions/assessment/{assessmentId}")
    @RolesAllowed({"INSTRUCTOR"})
    public List<Question> getQuestionsByAssessment(@PathVariable Long assessmentId) {
        return questionService.getQuestionsByAssessment(assessmentId);
    }
    @PutMapping("/questions/update/{questionId}")
    @RolesAllowed({"INSTRUCTOR"})
    public Question updateQuestion(@PathVariable Long questionId, @RequestBody Question updatedQuestion) {
        return questionService.updateQuestion(questionId, updatedQuestion);
    }
    @DeleteMapping("/questions/delete/{questionId}")
    @RolesAllowed({"INSTRUCTOR"})
    public void deleteQuestion(@PathVariable Long questionId) {
        questionService.deleteQuestion(questionId);
    }

    ///////////////

    // Course-related endpoints
    @PostMapping("/courses/create")
    @RolesAllowed({"INSTRUCTOR"})
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @PostMapping("/courses/{courseId}/lessons/add")
    @RolesAllowed({"INSTRUCTOR"})
    public Lesson addLesson(@PathVariable Long courseId, @RequestBody Lesson lesson) {
        return courseService.addLessonToCourse(courseId, lesson);
    }

    @DeleteMapping("/courses/{courseId}/lessons/delete/{lessonId}")
    @RolesAllowed({"INSTRUCTOR"})
    public void deleteLesson(@PathVariable Long courseId, @PathVariable Long lessonId) {
        courseService.deleteLessonFromCourse(courseId, lessonId);
    }

    @GetMapping("/courses/{courseId}/studentsEnrolled")
    @RolesAllowed({"INSTRUCTOR"})
    public List<String> getEnrolledStudents(@PathVariable Long courseId) {
        return courseService.getEnrolledStudents(courseId);
    }

    @PostMapping("/courses/{courseId}/lessons/{lessonId}/generateOtp")
    @RolesAllowed({"INSTRUCTOR"})
    public String generateOtp(@PathVariable Long courseId, @PathVariable Long lessonId) {
        return courseService.generateOtpForLesson(courseId, lessonId);
    }


    /////////////////////////////
    // profile-related endpoints
    @GetMapping("/viewInstructorProfile")
    @RolesAllowed({"INSTRUCTOR"})
    public ResponseEntity<?> getById(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the user ID from the token
        String token = authorizationHeader.substring(7); // Remove "Bearer "
        String email = jwtService.extractClaim(token, "sub"); // "sub" claim in token contains the email

        // Fetch the user by ID (if needed)
        User instructor = userServiceImp.getUserByEmail(email);

        // Return the user with HTTP status 200 OK
        return ResponseEntity.ok(instructor);
    }

    @PutMapping("/updateInstructorProfile")
    @RolesAllowed({"INSTRUCTOR"})
    public ResponseEntity<?> updateById(@RequestHeader("Authorization") String authorizationHeader,
                                        @RequestBody User user){
        String token = authorizationHeader.substring(7);
        String email = jwtService.extractClaim(token, "sub");

        // Fetch the user by ID (if needed)
        User instructor = userServiceImp.getUserByEmail(email);
        userServiceImp.updateById(instructor.getId(), user);
        user.setId(instructor.getId());
        return ResponseEntity.ok(user);
    }

    // Notification-related endpoints
    @GetMapping("/notifications")
    @RolesAllowed({"INSTRUCTOR"})
    public ResponseEntity<List<Notification>> getNotifications(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(required = false, defaultValue = "false") boolean unreadOnly) {
        String token = authorizationHeader.substring(7);
        String email = jwtService.extractClaim(token, "sub");
        User instructor = userServiceImp.getUserByEmail(email);
        List<Notification> notifications = notificationService.getNotificationsForUser(instructor.getId(), unreadOnly);

        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/notifications/preferences")
    @RolesAllowed({"INSTRUCTOR"})
    public ResponseEntity<String> updateNotificationPreferences(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody NotificationPreferences preferences) {
        String token = authorizationHeader.substring(7);
        String email = jwtService.extractClaim(token, "sub");
        User instructor = userServiceImp.getUserByEmail(email);
        notificationService.updateNotificationPreferences(instructor.getId(), preferences);

        return ResponseEntity.ok("Notification preferences updated successfully.");
    }



    // Enrollment notification endpoint
    @PostMapping("/notifications/enrollment")
    @RolesAllowed({"INSTRUCTOR"})
    public ResponseEntity<String> triggerEnrollmentNotification(@RequestParam int studentId, @RequestParam long courseId) {
        User student = userServiceImp.getUserById(studentId);
        Course course = courseService.getCourseById(courseId);
        notificationService.triggerEnrollmentConfirmation(studentId, courseId);
        return ResponseEntity.ok("Enrollment notification successfully for student: "
                + student.getName() + " in course: " + course.getTitle());    }


    // Course update notification
    @PostMapping("/courses/{courseId}/update")
    @RolesAllowed({"INSTRUCTOR"})
    public ResponseEntity<String> updateCourse(
            @PathVariable Long courseId,
            @RequestBody Course updatedCourse,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Check and extract token
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authorization token is missing or invalid.");
            }
            String token = authorizationHeader.substring(7); // Remove "Bearer "
            String email = jwtService.extractClaim(token, "sub");
            User instructor = userServiceImp.getUserByEmail(email);

            // Check if the user is an INSTRUCTOR
            if (!instructor.getRole().equals("INSTRUCTOR")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You are not authorized to update this course.");
            }

            // Get the existing course and update it
            Course existingCourse = courseService.getCourseById(courseId);
            if (existingCourse == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
            }

            existingCourse.setTitle(updatedCourse.getTitle());
            existingCourse.setDescription(updatedCourse.getDescription());
            courseService.saveCourse(existingCourse);

            // Trigger course update notification and send email
            notificationService.triggerCourseUpdateNotification(instructor.getId(), courseId);
            notificationService.sendCourseUpdateEmail(instructor.getEmail(), courseId);

            return ResponseEntity.ok("Course updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();  // Consider logging to a file or a monitoring service
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the course: " + e.getMessage());
        }
    }

    @PostMapping("/courses/{courseId}/send-update")
    @RolesAllowed({"INSTRUCTOR"})
    public ResponseEntity<String> sendCourseUpdate(@PathVariable Long courseId,
                                                   @RequestParam String updateMessage,
                                                   @RequestParam List<Long> studentIds) {
        for (Long studentId : studentIds) {
            notificationService.triggerCourseUpdateNotification(studentId, courseId, updateMessage);
        }
        return ResponseEntity.ok("Course updates sent successfully.");
    }

}