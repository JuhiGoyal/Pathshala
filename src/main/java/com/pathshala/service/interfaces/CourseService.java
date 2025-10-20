package com.pathshala.service.interfaces;

import com.pathshala.entities.Course;
import java.util.List;

public interface CourseService {

    List<Course> getCourseList(Integer userId);

    List<Course> search(String searchText, Integer userId);

    List<Course> findByIdIn(List<Integer> courseIdList);

    List<Course> getEnrolledCourseList(Integer userId);

    Course saveCourse(Course course);

    void deleteById(int id);
}
