package com.pathshala.service.interfaces;

import com.pathshala.entities.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionService {
    void generateQuestionsForCourseIfNotExists(Integer courseId, int count);

    List<Question> getQuestionsForCourse(Integer courseId);

    Optional<Question> assignQuestionToStudent(Integer courseId, Integer userId);

    Optional<Question> getAssignedQuestionForUser(Integer courseId, Integer userId);

    Optional<Question> findById(Long id);
}
