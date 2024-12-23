package com.example.lms.service;

import com.example.lms.DTO.QuizSubmissionRequest;
import com.example.lms.DTO.StudentAnswersRequest;
import com.example.lms.model.*;
import com.example.lms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GradesService {
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private GradesRepository gradesRepository;
    @Autowired
    private StudentAnswersRepository studentAnswersRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;

    //function for manual assessment grading
    public Submission gradeAssignment(Long submissionId, Double score, String feedback){
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(()-> new IllegalStateException("Submission with id" + submissionId + " doesn't exist."));
        if (!submission.getType().equalsIgnoreCase("assignment")){
            throw new IllegalStateException("The submission with id " + submissionId + " is not an assignment.");
        }
        submission.setScore(score);
        submission.setFeedback(feedback);
        Optional<Assessment> assessment = assessmentRepository.findById(submission.getAssessmentId());
        Long courseId = assessment.get().getCourseId();
        Grades grade = new Grades(submission.getStudentId(), submission.getAssessmentId(), score, feedback, courseId);
        gradesRepository.save(grade);
        return submissionRepository.save(submission);
    }

    //get all grades of a particular student
    public List<Grades> viewStudentGrades(Long studentid){
        List<Grades> gradesList = gradesRepository.findGradesByStudentId(studentid);
        if (gradesList == null || gradesList.isEmpty()){
            throw new IllegalStateException("No grades found for student with id " + studentid);
        }
        return gradesList;
    }

    //automatically grade a quiz by comparing the student's answer by the correct answers of the questions in the quiz
    public Submission autoGradeQuiz(Long submissionId){
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(()-> new IllegalStateException("Submission with id" + submissionId + " doesn't exist."));
        if (!submission.getType().equalsIgnoreCase("quiz")){
            throw new IllegalStateException("The submission with id " + submissionId + " is not a quiz.");
        }
        //get all answers of this submission
        List<StudentAnswers> studentAnswers = studentAnswersRepository.findStudentAnswersBySubmissionId(submissionId);

        //get all questions of this quiz
        List<Question> quizQuestions = questionRepository.findAllByAssessmentId(submission.getAssessmentId());

        // Map questions by their ID for easy lookup
        Map<Long, Question> questionMap = quizQuestions.stream()
                .collect(Collectors.toMap(Question::getQuestionId, question -> question));

        int totalQuizQuestions = quizQuestions.size();
        int correctAnswers = 0;
        for (StudentAnswers answer: studentAnswers){
            Question question = questionMap.get(answer.getQuestionId());
            if (question != null && question.getCorrectAnswer().equalsIgnoreCase(answer.getGivenAnswer())){
                correctAnswers++;
            }
        }
         Double score = (double) correctAnswers / totalQuizQuestions * 100;
        submission.setScore(score);
        String feedback;
        if (score > 80.0 && score <= 100.0){
            feedback = "Excellent";
        }
        else if(score >= 50.0 && score <= 80.0){
            feedback = "Good";
        }
        else{
            feedback = "Needs improvement";
        }
        submission.setFeedback(feedback);
        Optional<Assessment> assessment = assessmentRepository.findById(submission.getAssessmentId());
        Long courseId = assessment.get().getCourseId();
        Grades grade = new Grades(submission.getStudentId(), submission.getAssessmentId(), score, feedback, courseId);
        gradesRepository.save(grade);
        return submissionRepository.save(submission);
    }

    public Grades getQuizGrade(Long studentId, Long quizId){
        Grades quizGrade = gradesRepository.findGradeByStudentIdAndAssessmentId(studentId, quizId);
        if (quizGrade == null){
            throw new IllegalStateException("No grade found for student with id " + studentId + " for quiz with id " + quizId);
        }
        return quizGrade;
    }

    public Submission saveQuizSubmission(QuizSubmissionRequest quizSubmissionRequest){
        //save the submission
        Submission submission = new Submission();
        submission.setAssessmentId(quizSubmissionRequest.getQuizId());
        submission.setStudentId(quizSubmissionRequest.getStudentId());
        submission.setType("quiz");
        submissionRepository.save(submission);

        //save student answers
        for (StudentAnswersRequest answersRequest:quizSubmissionRequest.getStudentAnswers()){
            StudentAnswers studentAnswer = new StudentAnswers();
            studentAnswer.setSubmissionId(submission.getSubmissionId());
            studentAnswer.setStudentId(quizSubmissionRequest.getStudentId());
            studentAnswer.setQuestionId(answersRequest.getQuestionId());
            studentAnswer.setGivenAnswer(answersRequest.getAnswer());
            studentAnswersRepository.save(studentAnswer);
        }
        return submission;
    }

    public List<StudentAnswers> getstudentAnswers(Long studentId, Long submissionId){
        List<StudentAnswers> studentAnswersList = studentAnswersRepository.findStudentAnswersByStudentIdAndSubmissionId(studentId, submissionId);
        if (studentAnswersList == null || studentAnswersList.isEmpty()){
            throw new IllegalStateException("No answers for student with id " + studentId);
        }
        return studentAnswersList;
    }

    public List<Grades> viewStudentCourseGrades(Long studentId, Long courseId) {
        List<Grades> gradesList = gradesRepository.findGradesByStudentIdAndCourseId(studentId, courseId);
        if (gradesList == null || gradesList.isEmpty()){
            throw new IllegalStateException("No grades for student with id " + studentId);
        }
        return gradesList;
    }
}
