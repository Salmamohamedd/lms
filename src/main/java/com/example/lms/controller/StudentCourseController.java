package com.example.lms.controller;
import com.example.lms.model.Lesson;

import com.example.lms.model.Course;
import com.example.lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student/courses")
public class StudentCourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PostMapping("/{id}/enroll")
    public void enrollInCourse(@PathVariable Long id, @RequestParam String studentName) {
        courseService.enrollStudent(id, studentName);
    }


    @GetMapping("/{id}/lessons")
    public List<Lesson> getLessons(@PathVariable Long id) {
        return courseService.getLessonsForCourse(id);
    }

    @PostMapping("/{courseId}/lessons/{lessonId}/verify-otp")
    public boolean verifyOtp(@PathVariable Long courseId, @PathVariable Long lessonId, @RequestParam String otp) {
        return courseService.verifyOtp(courseId, lessonId, otp);
    }
}