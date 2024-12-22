
package com.example.lms.controller;
import com.example.lms.config.JwtService;
import com.example.lms.model.User;
import com.example.lms.model.Course;
import com.example.lms.model.Lesson;
import com.example.lms.service.ChartService;
import com.example.lms.service.CourseService;

import java.io.IOException;
import java.util.List;

import com.example.lms.service.PerformanceReportService;

import com.example.lms.service.CourseService;
import java.util.List;
import com.example.lms.repository.UserRepository;
import com.example.lms.service.AdminService;
import com.example.lms.service.UserServiceImp;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private PerformanceReportService performanceReportService;
    @Autowired
    private ChartService chartService;


    //performance analytics related endpoint
    @GetMapping("/report")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<byte[]> generateReport(@RequestParam Long courseId) throws IOException, IOException {
        byte[] report = performanceReportService.generatePerformanceReport(courseId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=performance_report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(report);
    }
    @GetMapping("/chart")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<byte[]> getPerformanceChart(@RequestParam Long courseId) throws IOException {
        byte[] chart = chartService.generatePerformanceChart(courseId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=performance_chart.png")
                .contentType(MediaType.IMAGE_PNG)
                .body(chart);
    }

    ///////////////////////////
    // user management endpoints
    @PostMapping("/createUser")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> createUser(@RequestBody User user) {

        User newUser = userServiceImp.createUser(user);
        String token = jwtService.generateToken(newUser);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/viewAdminProfile")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> getById(@RequestHeader("Authorization") String authorizationHeader) {
        // Extract the user ID from the token
        String token = authorizationHeader.substring(7); // Remove "Bearer "
        String email = jwtService.extractClaim(token, "sub"); // "sub" claim in token contains the email

        // Fetch the user by ID (if needed)
        User admin = userServiceImp.getUserByEmail(email);

        // Return the user with HTTP status 200 OK
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/viewProfile/{id}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> viewUserById(@PathVariable int id) {

        User user = userServiceImp.getUserById(id);

        return ResponseEntity.ok(user);

    }

    @PutMapping("/updateAdminProfile")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> updateById(@RequestHeader("Authorization") String authorizationHeader,
                                        @RequestBody User user){
        String token = authorizationHeader.substring(7); // Remove "Bearer "
        String email = jwtService.extractClaim(token, "sub"); // "sub" claim in token contains the email

        // Fetch the user by ID (if needed)
        User admin = userServiceImp.getUserByEmail(email);
        userServiceImp.updateById(admin.getId(), user);
        user.setId(admin.getId());
        return ResponseEntity.ok(user);

    }

    @PutMapping("/updateProfile/{id}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> updateUserById(@PathVariable int id,
                                            @RequestBody User user){

        userServiceImp.updateById(id, user);
        user.setId(id);
        return ResponseEntity.ok(user);

    }

    @DeleteMapping("/deleteUser/{id}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> deleteById(@PathVariable int id){

        userServiceImp.deleteById(id);
        return ResponseEntity.ok("deleted successfully");

    }

    // New Course-Related Endpoints

    @GetMapping("/getCourses")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @DeleteMapping("/deleteCourses/{id}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok("Course deleted successfully");
    }

    @GetMapping("/courseEnrolled/{id}/students")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<List<String>> getEnrolledStudents(@PathVariable Long id) {
        List<String> students = courseService.getEnrolledStudents(id);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/getLessons/{id}/lessons")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<List<Lesson>> getLessons(@PathVariable Long id) {
        List<Lesson> lessons = courseService.getLessonsForCourse(id);
        return ResponseEntity.ok(lessons);
    }


}


