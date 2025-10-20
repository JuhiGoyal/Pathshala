package com.pathshala.controller;

import com.pathshala.entities.AnswerSubmission;
import com.pathshala.entities.Question;
import com.pathshala.entities.User;
import com.pathshala.repository.AnswerSubmissionRepository;
import com.pathshala.service.interfaces.QuestionService;
import com.pathshala.utils.ExternalApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerSubmissionRepository answerSubmissionRepository;

    @Autowired
    private ExternalApiClient externalApiClient;

    /** Assign question to student when they enroll */
    @GetMapping("/assign/{courseId}")
    @ResponseBody
    public String assignForCourse(HttpSession session, @PathVariable("courseId") Integer courseId) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "NOT_LOGGED_IN";

        Optional<Question> q = questionService.assignQuestionToStudent(courseId, sessionUser.getId());
        return q.map(question -> "ASSIGNED:" + question.getId()).orElse("NO_QUESTION_AVAILABLE");
    }

    /** Open editor for assigned question */
    @GetMapping("/editor/{courseId}")
    public String openEditor(HttpSession session, @PathVariable("courseId") Integer courseId, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            model.addAttribute("error", "Please login to continue.");
            return "index";
        }

        Optional<Question> qOpt = questionService.getAssignedQuestionForUser(courseId, sessionUser.getId());
        if (!qOpt.isPresent()) {
            model.addAttribute("message", "No question assigned yet for this course. Please enroll first.");
            return "courses/courses";
        }

        Question q = qOpt.get();
        model.addAttribute("question", q);
        model.addAttribute("courseId", courseId);
        return "courses/notebook-editor";
    }

    /** Submit answer and check plagiarism */
    @PostMapping("/submit")
    public String submitAnswer(HttpSession session,
                               @RequestParam("questionId") Long questionId,
                               @RequestParam("courseId") Integer courseId,
                               @RequestParam("answerText") String answerText,
                               Model model) {

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            model.addAttribute("error", "Please login first.");
            return "index";
        }

        AnswerSubmission submission = answerSubmissionRepository.findByUserIdAndCourseId(sessionUser.getId(), courseId)
                .stream()
                .filter(s -> s.getQuestionId().equals(questionId))
                .findFirst()
                .orElse(null);

        if (submission == null) {
            submission = AnswerSubmission.builder()
                    .userId(sessionUser.getId())
                    .questionId(questionId)
                    .courseId(courseId)
                    .answerText(answerText)
                    .build();
        } else {
            submission.setAnswerText(answerText);
            submission.setSubmittedAt(java.time.LocalDateTime.now());
        }

        double score = externalApiClient.checkPlagiarism(answerText);
        submission.setPlagiarismScore(score);

        answerSubmissionRepository.save(submission);

        model.addAttribute("success", "Answer submitted. Plagiarism score: " + String.format("%.2f", score));
        model.addAttribute("plagiarismScore", score);
        model.addAttribute("question", questionService.findById(questionId).orElse(null));
        return "courses/submission-result";
    }
}
