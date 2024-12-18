package com.example.lms.repository;

import com.example.lms.model.Lesson;
import com.example.lms.model.Course;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class LessonRepository {
    private final List<Lesson> lessons = new ArrayList<>();
    private Long lessonIdCounter = 1L;

    public List<Lesson> findAll() {
        return lessons;
    }

    public Optional<Lesson> findById(Long id) {
        return lessons.stream().filter(lesson -> lesson.getId().equals(id)).findFirst();
    }

    public Lesson save(Lesson lesson, Course course) {
        if (lesson.getId() == null) {
            lesson.setId(lessonIdCounter++);
        }
        lessons.add(lesson);
        course.getLessons().add(lesson); // Associate the lesson with the course
        return lesson;
    }

    public void deleteById(Long id, Course course) {
        lessons.removeIf(lesson -> lesson.getId().equals(id));
        course.getLessons().removeIf(lesson -> lesson.getId().equals(id)); // Remove from course
    }

    public List<Lesson> findByCourse(Course course) {
        return course.getLessons(); // Return lessons associated with the course
    }
}