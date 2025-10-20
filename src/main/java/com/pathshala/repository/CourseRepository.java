package com.pathshala.repository;

import com.pathshala.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByCourseNameContainingIgnoreCase(String searchText);
    List<Course> findByIdIn(List<Integer> courseIdList);
}
