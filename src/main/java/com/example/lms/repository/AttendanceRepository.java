package com.example.lms.repository;

import com.example.lms.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findAttendanceByStudentIdAndCourseId(Long studentId, Long courseId);

    List<Attendance> findAllByCourseId(Long courseId);
}
