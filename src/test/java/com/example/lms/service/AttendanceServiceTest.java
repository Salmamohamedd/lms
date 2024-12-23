package com.example.lms.service;

import com.example.lms.model.Attendance;
import com.example.lms.model.Course;
import com.example.lms.model.Lesson;
import com.example.lms.repository.AttendanceRepository;
import com.example.lms.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private AttendanceService attendanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //test the creation of a new attendance record if none exists
    @Test
    void saveAttendance_shouldCreateNewAttendance() {
        Long courseId = 1L, studentId = 1L;
        Course course = new Course();
        course.setId(courseId);
        Lesson lesson1 = new Lesson(1L,"Lesson1","anything");
        Lesson lesson2 = new Lesson(2L,"Lesson2","anything");
        course.setLessons(List.of(lesson1,lesson2));

        //mock the courseRepository to return course with lessons
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        //mock the attendance repository to return an empty result
        when(attendanceRepository.findAttendanceByStudentIdAndCourseId(studentId, courseId)).thenReturn(Optional.empty());

        //save the attendance
        attendanceService.saveAttendance(courseId, studentId);

        //Use verify() to ensure the attendanceRepository.save() method was called, indicating a new record was created.
        verify(attendanceRepository, times(1)).save(any(Attendance.class));

    }

    //test the update of existing attendance
    @Test
    void saveAttendance_shouldUpdateExistingAttendance() {
        Long courseId = 1L, studentId = 1L;

        Course course = new Course();
        course.setId(courseId);
        Lesson lesson1 = new Lesson(1L, "Lesson1", "anything");
        Lesson lesson2 = new Lesson(2L, "Lesson2", "anything");
        Lesson lesson3 = new Lesson(3L, "Lesson3", "anything");
        course.setLessons(List.of(lesson1, lesson2, lesson3));

        Attendance attendance = new Attendance(studentId, courseId, 2, 1);

        //mock the courseRepository to return course with updated lessons 3
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        //mock the attendance repository to an existing attendance with a different total lessons count
        when(attendanceRepository.findAttendanceByStudentIdAndCourseId(studentId, courseId)).thenReturn(Optional.of(attendance));

        attendanceService.saveAttendance(courseId,studentId);

        //Verify that the Attendance object’s totalLessons is updated to match the course.
        assertEquals(3, attendance.getTotalLessons());
        //Check that lessonsAttended is incremented by 1, up to the total lessons.
        assertEquals(2, attendance.getLessonsAttended());
        //Use verify() to confirm attendanceRepository.save() was called to persist the changes.
        verify(attendanceRepository, times(1)).save(attendance);
    }

    @Test
    void getAttendancePercentage_shouldReturnCorrectAnswer() {
        Long courseId = 1L, studentId = 1L;

        Attendance attendance = new Attendance(studentId, courseId,4,2);

        when(attendanceRepository.findAttendanceByStudentIdAndCourseId(studentId, courseId)).thenReturn(Optional.of(attendance));

        double attendancePercentage = attendanceService.getAttendancePercentage(studentId, courseId);

        assertEquals(50.0, attendancePercentage);
    }

    @Test
    void updateAttendance() {
        Long attendanceId = 1L;

        Attendance attendance = new Attendance(1L, 1L, 2, 1);

        when(attendanceRepository.findById(attendanceId)).thenReturn(Optional.of(attendance));

        attendanceService.updateAttendance(attendanceId, 3,2);

        assertEquals(3, attendance.getTotalLessons());
        assertEquals(2, attendance.getLessonsAttended());
        verify(attendanceRepository, times(1)).save(attendance);
    }
}