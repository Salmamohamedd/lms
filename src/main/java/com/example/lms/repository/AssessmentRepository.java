package com.example.lms.repository;

import com.example.lms.model.Assessment;
import com.example.lms.model.AssessmentType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AssessmentRepository {

    private final List<Assessment> assessments = new ArrayList<>();
    private Long assessmentIdCounter = 1L;

    // Find all assessments
    public List<Assessment> findAll() {
        return assessments;
    }

    // Find an assessment by ID
    public Optional<Assessment> findById(Long id) {
        return assessments.stream().filter(assessment -> assessment.getAssessmentId().equals(id)).findFirst();
    }

    // Save or update an assessment
    public Assessment save(Assessment assessment) {
        if (assessment.getAssessmentId() == null) {  // Check if ID is null
            assessment.setAssessmentId(assessmentIdCounter++);
        }
        assessments.add(assessment);
        return assessment;
    }

    // Delete an assessment by ID
    public void deleteById(Long id) {
        assessments.removeIf(assessment -> assessment.getAssessmentId().equals(id));
    }

    // Find assessments by courseId
    public List<Assessment> findByCourseId(Long courseId) {
        List<Assessment> courseAssessments = new ArrayList<>();
        for (Assessment assessment : assessments) {
            if (assessment.getCourseId().equals(courseId)) {
                courseAssessments.add(assessment);
            }
        }
        return courseAssessments;
    }
    // Find the type of assessment by its ID
    public Optional<AssessmentType> findTypeByAssessmentId(Long assessmentId) {
        return assessments.stream()
                .filter(assessment -> assessment.getAssessmentId().equals(assessmentId))
                .map(Assessment::getType)
                .findFirst();
    }
}
