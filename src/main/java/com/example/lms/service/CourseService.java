

package com.example.lms.service;

import com.example.lms.model.Course;
import com.example.lms.model.Lesson;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.LessonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private AttendanceService attendanceService;

    // Get all courses
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Get course by ID
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
    }

    // Create a new course (Instructor only)
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    // Delete a course (Admin only)
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);  // Delete the course by ID
    }

    // Enroll a student in a course (Student only)
    public void enrollStudent(Long courseId, String studentName) {
        Course course = getCourseById(courseId);
        course.getEnrolledStudents().add(studentName);

    }

    public Lesson addLessonToCourse(Long courseId, Lesson lesson) {
        Course course = getCourseById(courseId);
        return lessonRepository.save(lesson, course);
    }

    // Delete a lesson from a course (Instructor only)
    public void deleteLessonFromCourse(Long courseId, Long lessonId) {
        Course course = getCourseById(courseId);
        lessonRepository.deleteById(lessonId, course);
    }

    // Get all lessons for a course (Admin, Instructor, Student)
    public List<Lesson> getLessonsForCourse(Long courseId) {
        Course course = getCourseById(courseId);
        return lessonRepository.findByCourse(course);
    }

    // Get enrolled students for a course (Admin, Instructor)
    public List<String> getEnrolledStudents(Long courseId) {
        return getCourseById(courseId).getEnrolledStudents();
    }

    public void generateOtpForLesson(Long courseId, Long lessonId) {
        Course course = getCourseById(courseId);
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        lesson.setOtp(generateRandomOtp());  // Use a method to generate a random OTP
    }

    private String generateRandomOtp() {
        return UUID.randomUUID().toString().substring(0, 6);  // Generate a 6-character OTP
    }


    public boolean verifyOtp(Long courseId, Long lessonId, Long studentId, String otp) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        if (lesson.getOtp().equals(otp)){
            attendanceService.saveAttendance(courseId, studentId);
            return true;
        }
        return false;
    }
}

