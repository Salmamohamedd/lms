package com.example.lms.service;

import com.example.lms.model.*;
import com.example.lms.repository.GradesRepository;
import com.example.lms.repository.QuestionRepository;
import com.example.lms.repository.StudentAnswersRepository;
import com.example.lms.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    //function for manual assessment grading
    public Submission gradeAssignment(Long submissionId, Double score, String feedback){
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(()-> new IllegalStateException("Submission with id" + submissionId + " doesn't exist."));
        if (!submission.getType().equalsIgnoreCase("assignment")){
            throw new IllegalStateException("The submission with id " + submissionId + " is not an assignment.");
        }
        submission.setScore(score);
        submission.setFeedback(feedback);

        Grades grade = new Grades(submission.getStudentId(), submission.getAssessmentId(), score, feedback);
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
        else if(score > 50.0 && score <= 80.0){
            feedback = "Good";
        }
        else{
            feedback = "Needs improvement";
        }
        submission.setFeedback(feedback);
        Grades grade = new Grades(submission.getStudentId(), submission.getAssessmentId(), score, feedback);
        gradesRepository.save(grade);
        return submissionRepository.save(submission);
    }

    public Grades getQuizGrade(Long studentId, Long quizId){
        Grades quizGrade = gradesRepository.findGradeByStudentIdAndAssessmentId(studentId, quizId);
        if (quizGrade == null){
            throw new IllegalStateException("No rade found for student with id " + studentId + " for quiz with id " + quizId);
        }
        return quizGrade;

    }
}
