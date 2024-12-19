package com.example.lms.service;

import com.example.lms.model.Grades;
import com.example.lms.repository.GradesRepository;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ChartService {

    @Autowired
    private GradesRepository gradesRepository;

    public byte[] generatePerformanceChart(Long courseId) throws IOException {
        List<Grades> gradesList = gradesRepository.findAllByCourseId(courseId);

        // Prepare Dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Grades grade : gradesList) {
            dataset.addValue(grade.getScore(), "Student " + grade.getStudentId(), "Assessment " + grade.getAssessmentId());
        }

        // Create Bar Chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Student Performance", "Assessments", "Scores", dataset);

        // Write Chart to ByteArrayOutputStream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(out, chart, 800, 600);
        return out.toByteArray();
    }
}
