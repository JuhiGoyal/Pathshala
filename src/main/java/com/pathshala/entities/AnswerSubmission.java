package com.pathshala.entities;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "answer_submission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "answer_text", length = 5000)
    private String answerText;


    @Column(name = "plagiarism_score", precision = 5, scale = 2)
    private Double plagiarismScore;
    @Column(name = "plagiarism_report_url", length = 1000)
    private String plagiarismReportUrl;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt = LocalDateTime.now();
}
