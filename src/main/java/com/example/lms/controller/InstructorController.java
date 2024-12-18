package com.example.lms.controller;


import com.example.lms.model.Assessment;
import com.example.lms.model.Question;
import com.example.lms.service.AssessmentService;
import com.example.lms.service.CourseService;
import com.example.lms.service.QuestionService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.lms.model.Lesson;
import com.example.lms.model.Course;
import com.example.lms.service.CourseService;
@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CourseService courseService;

    @PostMapping("/assessments/create")
    @RolesAllowed({"INSTRUCTOR"})
    public Assessment createAssessment(@RequestBody Assessment assessment) {
        return assessmentService.createAssessment(assessment);
    }

    @GetMapping("/assessments/all")
    @RolesAllowed({"INSTRUCTOR"})
    public List<Assessment> getAllAssessments() {
        return assessmentService.getAllAssessments();
    }

    @PutMapping("/assessments/update/{assessmentId}")
    @RolesAllowed({"INSTRUCTOR"})
    public Assessment updateAssessment(@PathVariable Long assessmentId, @RequestBody Assessment updatedAssessment) {
        return assessmentService.updateAssessment(assessmentId, updatedAssessment);
    }

    @DeleteMapping("/assessments/delete/{assessmentId}")
    @RolesAllowed({"INSTRUCTOR"})
    public void deleteAssessment(@PathVariable Long assessmentId) {
        assessmentService.deleteAssessment(assessmentId);
    }


    @PostMapping("/questions/add")
    @RolesAllowed({"INSTRUCTOR"})
    public Question addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }

    @GetMapping("/questions/assessment/{assessmentId}")
    @RolesAllowed({"INSTRUCTOR"})
    public List<Question> getQuestionsByAssessment(@PathVariable Long assessmentId) {
        return questionService.getQuestionsByAssessment(assessmentId);
    }
    @PutMapping("/questions/update/{questionId}")
    @RolesAllowed({"INSTRUCTOR"})
    public Question updateQuestion(@PathVariable Long questionId, @RequestBody Question updatedQuestion) {
        return questionService.updateQuestion(questionId, updatedQuestion);
    }

    ///////////////

    // Course-related endpoints
    @PostMapping("/courses/create")
    @RolesAllowed({"INSTRUCTOR"})
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @PostMapping("/courses/{courseId}/lessons/add")
    @RolesAllowed({"INSTRUCTOR"})
    public Lesson addLesson(@PathVariable Long courseId, @RequestBody Lesson lesson) {
        return courseService.addLessonToCourse(courseId, lesson);
    }

    @DeleteMapping("/courses/{courseId}/lessons/delete/{lessonId}")
    @RolesAllowed({"INSTRUCTOR"})
    public void deleteLesson(@PathVariable Long courseId, @PathVariable Long lessonId) {
        courseService.deleteLessonFromCourse(courseId, lessonId);
    }

    @GetMapping("/courses/{courseId}/studentsEnrolled")
    @RolesAllowed({"INSTRUCTOR"})
    public List<String> getEnrolledStudents(@PathVariable Long courseId) {
        return courseService.getEnrolledStudents(courseId);
    }

    @PostMapping("/courses/{courseId}/lessons/{lessonId}/generateOtp")
    @RolesAllowed({"INSTRUCTOR"})
    public void generateOtp(@PathVariable Long courseId, @PathVariable Long lessonId) {
        courseService.generateOtpForLesson(courseId, lessonId);
    }

}



