package com.example.lms.service;

import com.example.lms.model.Attendance;
import com.example.lms.model.Grades;
import com.example.lms.repository.AttendanceRepository;
import com.example.lms.repository.GradesRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PerformanceReportService {

    @Autowired
    private GradesRepository gradesRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    public byte[] generatePerformanceReport(Long courseId) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Student Performance");

        // Header Row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Student ID");
        header.createCell(1).setCellValue("Assessment ID");
        header.createCell(2).setCellValue("Score");
        header.createCell(3).setCellValue("Feedback");
        header.createCell(4).setCellValue("Attendance %");

        // Fetch Grades and Attendance
        List<Grades> gradesList = gradesRepository.findAllByCourseId(courseId);
        List<Attendance> attendanceList = attendanceRepository.findAllByCourseId(courseId);

        // Create a map of attendance for quick lookup
        Map<Long, Double> attendanceMap = attendanceList.stream()
                .collect(Collectors.toMap(
                        Attendance::getStudentId,
                        attendance -> (double) attendance.getLessonsAttended() / attendance.getTotalLessons() * 100
                ));

        // Fill Data Rows
        int rowIdx = 1;
        for (Grades grade : gradesList) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(grade.getStudentId());
            row.createCell(1).setCellValue(grade.getAssessmentId());
            row.createCell(2).setCellValue(grade.getScore());
            row.createCell(3).setCellValue(grade.getFeedback());

            Double attendancePercentage = attendanceMap.getOrDefault(grade.getStudentId(), 0.0);
            row.createCell(4).setCellValue(attendancePercentage);
        }

        // Write to ByteArrayOutputStream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }
}
