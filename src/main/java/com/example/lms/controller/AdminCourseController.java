package com.example.lms.controller;
import com.example.lms.model.Lesson;
import com.example.lms.model.Course;
import com.example.lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/admin/courses")
public class AdminCourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    @GetMapping("/{id}/students")
    public List<String> getEnrolledStudents(@PathVariable Long id) {
        return courseService.getEnrolledStudents(id);
    }

    @GetMapping("/{id}/lessons")
    public List<Lesson> getLessons(@PathVariable Long id) {
        return courseService.getLessonsForCourse(id);
    }
}