package com.pathshala.controller;

import com.pathshala.entities.Course;
import com.pathshala.entities.CourseEnrollment;
import com.pathshala.entities.User;
import com.pathshala.service.impl.CourseServiceImpl;
import com.pathshala.service.impl.QuestionServiceImpl;
import com.pathshala.service.interfaces.CourseService;
import com.pathshala.service.interfaces.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.pathshala.repository.CourseEnrollmentRepository;
import javax.servlet.http.HttpSession;
import java.util.List;
import com.pathshala.utils.ExternalApiClient;
@Controller
@Slf4j
public class CourseController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private ExternalApiClient externalApiClient;
	@Autowired
	private QuestionService questionService;
    @Autowired
    private QuestionServiceImpl questionServiceImpl;
    @Autowired
    private CourseServiceImpl courseServiceImpl;

	/** Show Add Course Page */
	@GetMapping("/admin/add-course")
	public String addCoursePage(Model model) {
		model.addAttribute("course", new Course());
		return "courses/add-course";
	}
	// For admin: open manage course page

	@PostMapping("/init-manage-course")
	public String initManageCourse(@ModelAttribute("course") Course course, Model model) {
		// fetch course by ID from DB
		Course dbCourse = courseServiceImpl.findById(course.getId())
				.orElseThrow(() -> new RuntimeException("Course not found"));

		model.addAttribute("courseList", List.of(dbCourse)); // display single course for admin to manage
		model.addAttribute("title", "Manage Course");
		return "manage-course";
	}

	@PostMapping("/generate-questions")
	public String generateQuestions(@RequestParam("courseId") Integer courseId,
									@RequestParam("topic") String topic,
									@RequestParam("numQuestions") int numQuestions,
									Model model) {
		try {
			List<String> questions = externalApiClient.generateQuestionsForCourse(courseId, numQuestions);
			boolean assigned = questionServiceImpl.assignQuestionsToStudent(courseId, questions);
			if(assigned){
				model.addAttribute("success", "Questions generated and assigned successfully!");
			} else {
				model.addAttribute("error", "Failed to assign questions. Try again.");
			}
		} catch(Exception e){
			log.error("Error generating questions", e);
			model.addAttribute("error", "Internal server error. Check logs.");
		}
		// Pass course list back to template
		model.addAttribute("courses", courseServiceImpl.findAllCourses());
		return "generate-question";
	}

	// For user: open course detail page
	@PostMapping("/init-course-detail")
	public String initCourseDetail(@ModelAttribute("course") Course course, Model model, HttpSession session) {
		Course dbCourse = courseServiceImpl.findById(course.getId())
				.orElseThrow(() -> new RuntimeException("Course not found"));

		model.addAttribute("course", dbCourse);

		// Check if user is enrolled
		User sessionUser = (User) session.getAttribute("user");
		boolean isEnrolled = false;
		if(sessionUser != null){
			isEnrolled = courseEnrollmentRepository.getCourseEnrollmentByUserId(sessionUser.getId())
					.stream()
					.anyMatch(e -> e.getCourseId().equals(course.getId()));
		}
		model.addAttribute("isEnrolled", isEnrolled);

		return "course-details";
	}



	// Enroll a user into a course
	@Autowired
	private CourseEnrollmentRepository courseEnrollmentRepository; // inject instance

	@GetMapping("/enroll/{id}")
	public String enrollCourse(HttpSession session, @PathVariable("id") Integer courseId, Model model) {
		User sessionUser = (User) session.getAttribute("user");
		if (sessionUser == null) {
			model.addAttribute("error", "Please login first to enroll.");
			return "index";
		}

		// Check if already enrolled
		List<CourseEnrollment> existing = courseEnrollmentRepository.getCourseEnrollmentByUserId(sessionUser.getId())
				.stream()
				.filter(e -> e.getCourseId().equals(courseId))
				.toList();

		if (existing.isEmpty()) {
			CourseEnrollment enrollment = new CourseEnrollment();
			enrollment.setCourseId(courseId);
			enrollment.setUserId(sessionUser.getId());
			courseEnrollmentRepository.save(enrollment); // use instance, not class
		}

		return "redirect:/courses"; // redirect to course listing page
	}


	/** Submit new course */
	@PostMapping("/submit-add-course")
	public String submitAddCourse(@ModelAttribute("course") Course course, Model model) {
		Course savedCourse = courseService.saveCourse(course);

		int defaultQuestionCount = 5; // questions generated automatically
		questionService.generateQuestionsForCourseIfNotExists(savedCourse.getId(), defaultQuestionCount);

		model.addAttribute("success", "Course added successfully and default questions generated!");
		model.addAttribute("course", new Course());
		return "courses/add-course";
	}
}
