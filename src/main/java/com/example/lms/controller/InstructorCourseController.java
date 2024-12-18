package com.example.lms.controller;
import com.example.lms.model.Lesson;
import com.example.lms.model.Course;
import com.example.lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instructor/courses")
public class InstructorCourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }


    @PostMapping("/{id}/lessons")
    public Lesson addLesson(@PathVariable Long id, @RequestBody Lesson lesson) {
        // Add the lesson to the course
        return courseService.addLessonToCourse(id, lesson);
    }

    @DeleteMapping("/{courseId}/lessons/{lessonId}")
    public void deleteLesson(@PathVariable Long courseId, @PathVariable Long lessonId) {
        courseService.deleteLessonFromCourse(courseId, lessonId);
    }

    @GetMapping("/{id}/students")
    public List<String> getEnrolledStudents(@PathVariable Long id) {
        return courseService.getEnrolledStudents(id);
    }

    @PostMapping("/{courseId}/lessons/{lessonId}/generate-otp")
    public void generateOtp(@PathVariable Long courseId, @PathVariable Long lessonId) {
        courseService.generateOtpForLesson(courseId, lessonId);
    }
}