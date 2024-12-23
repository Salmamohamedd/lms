package com.example.lms.service;
import com.example.lms.model.Course;
import com.example.lms.model.Lesson;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CourseAttendanceIntegrationTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    private Course course;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        // Create and save a new course for testing
        course = new Course();
        course.setTitle("Java 101");
        course.setDescription("Intro to Java");
        course.setDuration("30 hours");
        course = courseRepository.save(course);  // Save the course to the database

        // Create and save a lesson for testing
        lesson = new Lesson();
        lesson.setTitle("Lesson 1: Introduction to Java");
        lesson.setCourse(course);
        lesson = lessonRepository.save(lesson,course);  // Save the lesson to the database
    }

    @Test
    void testVerifyOtpAndMarkAttendance() {
        // Given a student name and OTP for the lesson
        String studentName = "John Doe";
        courseService.enrollStudent(course.getId(), studentName);
        String otp = courseService.generateOtpForLesson(course.getId(), lesson.getId());

        // When verifying OTP and marking attendance
        boolean isVerified = courseService.verifyOtp(course.getId(), lesson.getId(), 1L, otp);  // Assuming student ID is 1

        // Then the OTP should be verified, and attendance should be marked
        assertTrue(isVerified, "OTP should be verified successfully");

        double attendancePercentage = attendanceService.getAttendancePercentage(1L, course.getId());
        assertEquals(100.0, attendancePercentage, "Attendance should be marked correctly after OTP verification");
    }


}
