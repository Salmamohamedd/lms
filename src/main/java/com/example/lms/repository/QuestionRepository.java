package com.example.lms.repository;

import com.example.lms.model.Question;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class QuestionRepository {
    private final List<Question> questions = new ArrayList<>();
    private Long questionIdCounter = 1L;

    public List<Question> findAll() {
        return questions;
    }

    public Optional<Question> findById(Long id) {
        return questions.stream().filter(question -> question.getQuestionId().equals(id)).findFirst();
    }

    public List<Question> findAllByAssessmentId(Long assessmentId) {
        List<Question> result = new ArrayList<>();
        for (Question question : questions) {
            if (question.getAssessmentId().equals(assessmentId)) {
                result.add(question);
            }
        }
        return result;
    }

    public Question save(Question question) {
        if (question.getQuestionId() == null) {
            question.setQuestionId(questionIdCounter++);
        }
        questions.add(question);
        return question;
    }

    public void deleteById(Long id) {
        questions.removeIf(question -> question.getQuestionId().equals(id));
    }
<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
