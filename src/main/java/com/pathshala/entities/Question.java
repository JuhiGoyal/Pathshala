package com.pathshala.entities;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "question")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "question_text", length = 2000)
    private String questionText;

    @Column(name = "assigned")
    private boolean assigned = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public String getAssignedStudents() {
        return "";
    }
}
