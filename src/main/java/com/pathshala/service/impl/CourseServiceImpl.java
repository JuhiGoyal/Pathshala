package com.pathshala.service.impl;

import com.pathshala.entities.Course;
import com.pathshala.entities.CourseEnrollment;
import com.pathshala.repository.CourseEnrollmentRepository;
import com.pathshala.repository.CourseRepository;
import com.pathshala.repository.file.FileCourseRepository;
import com.pathshala.service.interfaces.CourseService;
import com.pathshala.service.interfaces.QuestionService;
import com.pathshala.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FileCourseRepository fileCourseRepository;

    @Autowired
    private CourseEnrollmentRepository courseEnrollmentRepository;

    @Autowired
    private QuestionService questionService;

    @Override
    public List<Course> getCourseList(Integer userId) {
        List<Integer> enrolledCourseIds = getEnrolledCourseIds(userId);
        List<Course> courseList = courseRepository.findAll();
        return stampImageIdAndEnrollmentFlag(courseList, enrolledCourseIds);
    }

    @Override
    public List<Course> search(String searchText, Integer userId) {
        List<Integer> enrolledCourseIds = getEnrolledCourseIds(userId);
        List<Course> courseList = courseRepository.findByCourseNameContainingIgnoreCase(searchText);
        return stampImageIdAndEnrollmentFlag(courseList, enrolledCourseIds);
    }

    private List<Course> stampImageIdAndEnrollmentFlag(List<Course> courseList, List<Integer> enrolledCourseIds) {
        return Utils.safe(courseList).stream().map(c -> {
            c.setImageId(String.valueOf(c.getId() % 10));
            c.setEnrolled(enrolledCourseIds.contains(c.getId()));
            return c;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Course> findByIdIn(List<Integer> courseIdList) {
        return courseRepository.findByIdIn(courseIdList);
    }

    @Override
    public List<Course> getEnrolledCourseList(Integer userId) {
        List<Integer> enrolledCourseIds = getEnrolledCourseIds(userId);
        List<Course> courseList = findByIdIn(enrolledCourseIds);
        return stampImageIdAndEnrollmentFlag(courseList, enrolledCourseIds);
    }

    @Override
    public Course saveCourse(Course course) {
        boolean isNew = (course.getId() == null || course.getId() == 0);
        course = courseRepository.save(course);
        fileCourseRepository.save(course);

        if (isNew) {
            try {
                // Generate 50 questions for the new course
                questionService.generateQuestionsForCourseIfNotExists(course.getId(), 50);
            } catch (Exception e) {
                log.error("Error generating questions for course {} : {}", course.getId(), e.getMessage());
            }
        }

        return course;
    }

    @Override
    public void deleteById(int id) {
        courseRepository.deleteById(id);
        fileCourseRepository.deleteById(id);
    }
    // Find by ID
    public Optional<Course> findById(Integer id) {
        return courseRepository.findById(id);
    }

    // Find all courses (for generate-question page)
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    private List<Integer> getEnrolledCourseIds(Integer userId) {
        if (userId != null && userId > 0) {
            List<CourseEnrollment> courseEnrollmentList = courseEnrollmentRepository.getCourseEnrollmentByUserId(userId);
            return Utils.safe(courseEnrollmentList).stream().map(CourseEnrollment::getCourseId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
