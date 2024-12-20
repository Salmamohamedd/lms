package com.example.lms.service;

import com.example.lms.model.Attendance;
import com.example.lms.model.Course;
import com.example.lms.repository.AttendanceRepository;
import com.example.lms.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private CourseRepository courseRepository;

    public void saveAttendance(Long courseId, Long studentId){
        //get or create an attendance record
        Optional<Attendance> optionalAttendance = attendanceRepository.findAttendanceByStudentIdAndCourseId(studentId, courseId);
        Attendance attendance;
        //if an attendance record is found then get it
        if (optionalAttendance.isPresent()){
            attendance = optionalAttendance.get();
        }
        //if no record is found then create a new record
        else{
            Optional<Course> course = courseRepository.findById(courseId);
            int totalLessons = course.get().getLessons().size();
            attendance = new Attendance(studentId, courseId, totalLessons, 0);
        }
        // Mark the lesson as attended if not already marked
        if (attendance.getLessonsAttended() < attendance.getTotalLessons()) {
            attendance.setLessonsAttended(attendance.getLessonsAttended() + 1);
        }
        else {
            throw new RuntimeException("All lessons have already been attended for this course.");
        }
        attendanceRepository.save(attendance);
    }

    public double getAttendancePercentage(Long studentId, Long courseId){
        Attendance attendance = attendanceRepository.findAttendanceByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(()-> new IllegalStateException("Attendance record not found for student with id " + studentId + " in course with id " + courseId));
        if (attendance.getTotalLessons() == 0.0)
            return 0.0;
        return (double) attendance.getLessonsAttended() / attendance.getTotalLessons() * 100;
    }

    //update attendance manually
    public void updateAttendance(Long attendanceId, int totalLessons, int lessonsAttended){
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(()-> new IllegalStateException("Attendance record not found for student with id " + attendanceId));
        attendance.setTotalLessons(totalLessons);
        attendance.setLessonsAttended(lessonsAttended);
        attendanceRepository.save(attendance);
    }
}
