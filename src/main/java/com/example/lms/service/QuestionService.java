package com.example.lms.service;

import com.example.lms.model.Question;
import com.example.lms.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    // Add a question
    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    // Get all questions by assessmentId
    public List<Question> getQuestionsByAssessment(Long assessmentId) {
        return questionRepository.findAllByAssessmentId(assessmentId);
    }

    // Update an existing question by questionId
    public Question updateQuestion(Long questionId, Question updatedQuestion) {
        return questionRepository.findById(questionId)
                .map(existingQuestion -> {
                    existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
                    existingQuestion.setType(updatedQuestion.getType());
                    existingQuestion.setOptions(updatedQuestion.getOptions());
                    existingQuestion.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
                    return questionRepository.save(existingQuestion);
                })
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    // Delete a question by questionId
    public void deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
    }
<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
