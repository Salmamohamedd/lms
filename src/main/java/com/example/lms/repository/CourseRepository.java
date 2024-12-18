package com.example.lms.repository;

import com.example.lms.model.Course;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepository {
    private final List<Course> courses = new ArrayList<>();
    private Long courseIdCounter = 1L;

    public List<Course> findAll() {
        return courses;
    }

    public Optional<Course> findById(Long id) {
        return courses.stream().filter(course -> course.getId().equals(id)).findFirst();
    }

    public Course save(Course course) {
        if (course.getId() == null) {
            course.setId(courseIdCounter++);
        }
        courses.add(course);
        return course;
    }

    public void deleteById(Long id) {
        courses.removeIf(course -> course.getId().equals(id));
    }
}