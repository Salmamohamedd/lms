
package com.example.lms.service;

import com.example.lms.model.Course;
import com.example.lms.model.Lesson;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private AttendanceService attendanceService;

    private Course course;
    private Lesson lesson;
    private Long lessonId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        course = new Course();
        course.setTitle("Java 101");
        course.setDescription("Intro to Java");
        course.setDuration("30 hours");

        lesson = new Lesson();
        lesson.setTitle("Lesson 1");
//        lesson.setDescription("Introduction to Java");
    }

    @Test
    void testGetAllCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> result = courseService.getAllCourses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java 101", result.get(0).getTitle());
    }

    @Test
    void testGetCourseById() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course result = courseService.getCourseById(1L);

        assertNotNull(result);
        assertEquals("Java 101", result.getTitle());
    }

    @Test
    void testCreateCourse() {
        when(courseRepository.save(course)).thenReturn(course);

        Course result = courseService.createCourse(course);

        assertNotNull(result);
        assertEquals("Java 101", result.getTitle());
    }

    @Test
    void testDeleteCourse() {
        doNothing().when(courseRepository).deleteById(1L);

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEnrollStudent() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.enrollStudent(1L, "John Doe");

        assertTrue(course.getEnrolledStudents().contains("John Doe"));
    }

    @Test
    void testAddLessonToCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.save(lesson,course)).thenReturn(lesson);

        Lesson result = courseService.addLessonToCourse(1L, lesson);

        assertNotNull(result);
        assertEquals("Lesson 1", result.getTitle());
    }

    @Test
    void testDeleteLessonFromCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        doNothing().when(lessonRepository).deleteById(1L,course);

        courseService.deleteLessonFromCourse(1L, 1L);

        verify(lessonRepository, times(1)).deleteById(1L,course);
    }

    @Test
    void testGetLessonsForCourse() {
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.findByCourse(course)).thenReturn(lessons);

        List<Lesson> result = courseService.getLessonsForCourse(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Lesson 1", result.get(0).getTitle());
    }

    @Test
    void testGetEnrolledStudents() {
        course.getEnrolledStudents().add("John Doe");
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        List<String> result = courseService.getEnrolledStudents(1L);

        assertNotNull(result);
        assertTrue(result.contains("John Doe"));
    }

    @Test
    void testGenerateOtpForLesson() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        String otp = courseService.generateOtpForLesson(1L, 1L);

        assertNotNull(otp);
        assertEquals(6, otp.length());
    }

    @Test
    void testVerifyOtp() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        lesson.setOtp("123456");

        boolean result = courseService.verifyOtp(1L, 1L, 1L, "123456");

        assertTrue(result);
        verify(attendanceService, times(1)).saveAttendance(1L, 1L);
    }

    @Test
    void testSaveCourse() {
        when(courseRepository.save(course)).thenReturn(course);

        Course result = courseService.saveCourse(course);

        assertNotNull(result);
        assertEquals("Java 101", result.getTitle());
    }
}
