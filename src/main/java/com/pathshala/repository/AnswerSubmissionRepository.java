package com.pathshala.repository;

import com.pathshala.entities.AnswerSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnswerSubmissionRepository extends JpaRepository<AnswerSubmission, Long> {
    List<AnswerSubmission> findByUserIdAndCourseId(Integer userId, Integer courseId);
    List<AnswerSubmission> findByQuestionId(Long questionId);
}
