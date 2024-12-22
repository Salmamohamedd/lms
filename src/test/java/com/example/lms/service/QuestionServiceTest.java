package com.example.lms.service;

import com.example.lms.model.Question;
import com.example.lms.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestionServiceTest {

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addQuestion() {
        Question question = new Question();
        question.setQuestionText("What is Java?");
        question.setCorrectAnswer("Programming Language");

        when(questionRepository.save(any(Question.class))).thenReturn(question);

        Question savedQuestion = questionService.addQuestion(question);

        assertNotNull(savedQuestion);
        assertEquals("What is Java?", savedQuestion.getQuestionText());
        assertEquals("Programming Language", savedQuestion.getCorrectAnswer());
        verify(questionRepository, times(1)).save(question);
    }

    @Test
    void getQuestionsByAssessment() {
        Question question1 = new Question();
        question1.setQuestionText("What is Java?");
        Question question2 = new Question();
        question2.setQuestionText("What is Python?");

        List<Question> questions = Arrays.asList(question1, question2);

        when(questionRepository.findAllByAssessmentId(1L)).thenReturn(questions);

        List<Question> result = questionService.getQuestionsByAssessment(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("What is Java?", result.get(0).getQuestionText());
        assertEquals("What is Python?", result.get(1).getQuestionText());
        verify(questionRepository, times(1)).findAllByAssessmentId(1L);
    }

    @Test
    void updateQuestion() {
        Question existingQuestion = new Question();
        existingQuestion.setQuestionText("Old Question");
        existingQuestion.setCorrectAnswer("Old Answer");

        Question updatedQuestion = new Question();
        updatedQuestion.setQuestionText("Updated Question");
        updatedQuestion.setCorrectAnswer("Updated Answer");

        when(questionRepository.findById(1L)).thenReturn(Optional.of(existingQuestion));
        when(questionRepository.save(any(Question.class))).thenReturn(existingQuestion);

        Question result = questionService.updateQuestion(1L, updatedQuestion);

        assertNotNull(result);
        assertEquals("Updated Question", result.getQuestionText());
        assertEquals("Updated Answer", result.getCorrectAnswer());
        verify(questionRepository, times(1)).findById(1L);
        verify(questionRepository, times(1)).save(existingQuestion);
    }

    @Test
    void deleteQuestion() {
        doNothing().when(questionRepository).deleteById(1L);

        questionService.deleteQuestion(1L);

        verify(questionRepository, times(1)).deleteById(1L);
    }
}
