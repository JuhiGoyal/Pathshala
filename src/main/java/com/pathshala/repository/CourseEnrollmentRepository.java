package com.pathshala.repository;

import com.pathshala.entities.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Integer> {
    List<CourseEnrollment> getCourseEnrollmentByUserId(Integer userId);
    List<CourseEnrollment> getCourseEnrollmentByCourseId(Integer courseId);
}
