package com.pathshala.repository;

import com.pathshala.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCourseId(Integer courseId);
    List<Question> findByCourseIdAndAssignedFalse(Integer courseId);
    Optional<Question> findByIdAndAssignedFalse(Long id);
    Optional<Question> findById(Long id);
}
