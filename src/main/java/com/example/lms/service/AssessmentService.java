package com.example.lms.service;

import com.example.lms.model.Assessment;
import com.example.lms.repository.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    // Create or update an assessment
    public Assessment createAssessment(Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    // Get all assessments
    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    // Get an assessment by ID
    public Assessment getAssessmentById(Long assessmentId) {
        return assessmentRepository.findById(assessmentId).orElseThrow(() -> new RuntimeException("Assessment not found"));
    }

    // Update an assessment
    public Assessment updateAssessment(Long assessmentId, Assessment updatedAssessment) {
        Assessment existingAssessment = getAssessmentById(assessmentId);
        existingAssessment.setTitle(updatedAssessment.getTitle());
        existingAssessment.setType(updatedAssessment.getType());
        existingAssessment.setDateCreated(updatedAssessment.getDateCreated());
        return assessmentRepository.save(existingAssessment);
    }

    // Delete an assessment
    public void deleteAssessment(Long assessmentId) {
        assessmentRepository.deleteById(assessmentId);
    }

    // Get assessments by course ID
    public List<Assessment> getAssessmentsByCourse(Long courseId) {
        return assessmentRepository.findByCourseId(courseId);
    }
}