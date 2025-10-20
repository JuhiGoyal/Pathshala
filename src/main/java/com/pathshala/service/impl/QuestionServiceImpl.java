package com.pathshala.service.impl;

import com.pathshala.entities.Question;
import com.pathshala.repository.QuestionRepository;
import com.pathshala.service.interfaces.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public void generateQuestionsForCourseIfNotExists(Integer courseId, int count) {
        List<Question> existing = questionRepository.findByCourseId(courseId);
        if (!existing.isEmpty()) return;

        for (int i = 1; i <= count; i++) {
            Question q = new Question();
            q.setCourseId(courseId);
            q.setQuestionText("Default Question " + i + " for course " + courseId);
            questionRepository.save(q);
        }
    }
    // Generate questions for a course
    public List<String> generateQuestionsForCourse(Integer courseId, int count) {
        // You can call ExternalApiClient from here if needed
        List<String> questions = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            questions.add("Q" + i + " for course " + courseId);
        }
        return questions;
    }

    // Assign generated questions to student
    public boolean assignQuestionsToStudent(Integer courseId, List<String> questions) {
        try {
            for (String qText : questions) {
                Question q = new Question();
                q.setCourseId(courseId);
                q.setQuestionText(qText);
                questionRepository.save(q);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Question> getQuestionsForCourse(Integer courseId) {
        return questionRepository.findByCourseId(courseId);
    }

    @Override
    public Optional<Question> assignQuestionToStudent(Integer courseId, Integer userId) {
        // Pick first question for course (for simplicity)
        List<Question> questions = questionRepository.findByCourseId(courseId);
        if (questions.isEmpty()) return Optional.empty();
        return Optional.of(questions.get(0)); // Assign first question
    }

    @Override
    public Optional<Question> getAssignedQuestionForUser(Integer courseId, Integer userId) {
        // For simplicity, return first question (no extra mapping table)
        List<Question> questions = questionRepository.findByCourseId(courseId);
        if (questions.isEmpty()) return Optional.empty();
        return Optional.of(questions.get(0));
    }

    @Override
    public Optional<Question> findById(Long id) {
        return questionRepository.findById(id);
    }
}
