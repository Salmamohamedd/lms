

package com.example.lms.controller;

import com.example.lms.model.Course;
import com.example.lms.model.Lesson;

import com.example.lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // Admin and Instructor: View all courses
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    // Instructor: Create a course
    @PostMapping("/create")
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    // Instructor: Add a lesson to a course
    @PostMapping("/{courseId}/lessons")
    public Lesson addLessonToCourse(@PathVariable Long courseId, @RequestBody Lesson lesson) {
        return courseService.addLessonToCourse(courseId, lesson);
    }

    // Student: Enroll in a course
    @PostMapping("/{courseId}/enroll")
    public void enrollInCourse(@PathVariable Long courseId, @RequestParam String studentName) {
        courseService.enrollStudent(courseId, studentName);
    }

    // Instructor: Generate OTP for a lesson
    @PostMapping("/{courseId}/lessons/{lessonId}/generate-otp")
    public void generateOtp(@PathVariable Long courseId, @PathVariable Long lessonId) {
        courseService.generateOtpForLesson(courseId, lessonId);
    }

    // Student: Verify OTP for attendance
    @PostMapping("/{courseId}/lessons/{lessonId}/verify-otp")
    public boolean verifyOtp(@PathVariable Long courseId, @PathVariable Long lessonId, @RequestParam String otp) {
        return courseService.verifyOtp(courseId, lessonId, otp);
    }
}